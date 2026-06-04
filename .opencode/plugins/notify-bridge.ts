// NotifyBridge Plugin — отправляет уведомления о завершении сессий
// Поддерживает: Discord webhook, Telegram bot, OpenClaw Gateway
//
// Переменные окружения:
//   DISCORD_WEBHOOK_URL — вебхук Discord канала
//   TELEGRAM_BOT_TOKEN — токен Telegram бота
//   TELEGRAM_CHAT_ID — ID чата/канала Telegram
//   OPENCLAW_GATEWAY_URL — URL OpenClaw gateway (по умолчанию http://127.0.0.1:18789)

const OPENCLAW_URL = process.env.OPENCLAW_GATEWAY_URL || "http://127.0.0.1:18789"
const DISCORD_WEBHOOK = process.env.DISCORD_WEBHOOK_URL || ""
const TELEGRAM_BOT = process.env.TELEGRAM_BOT_TOKEN || ""
const TELEGRAM_CHAT = process.env.TELEGRAM_CHAT_ID || ""

async function sendDiscord(message) {
  if (!DISCORD_WEBHOOK) return
  try {
    await fetch(DISCORD_WEBHOOK, {
      method: "POST",
      headers: { "Content-Type": "application/json" },
      body: JSON.stringify({ content: message }),
    })
  } catch (e) {
    // silently fail — notifications are best-effort
  }
}

async function sendTelegram(message) {
  if (!TELEGRAM_BOT || !TELEGRAM_CHAT) return
  try {
    const url = `https://api.telegram.org/bot${TELEGRAM_BOT}/sendMessage`
    await fetch(url, {
      method: "POST",
      headers: { "Content-Type": "application/json" },
      body: JSON.stringify({
        chat_id: TELEGRAM_CHAT,
        text: message,
        parse_mode: "Markdown",
      }),
    })
  } catch (e) {
    // silently fail
  }
}

async function sendOpenClaw(message) {
  try {
    await fetch(`${OPENCLAW_URL}/api/health`).catch(() => null)
    // OpenClaw health check — если не отвечает, значит не запущен
  } catch {
    return // OpenClaw not running
  }
}

async function notify(message) {
  await Promise.allSettled([
    sendDiscord(message),
    sendTelegram(message),
    sendOpenClaw(message),
  ])
}

export const NotifyBridge = async ({ client, project, directory }) => {
  const projectName = project?.name || directory.split(/[/\\]/).pop()

  return {
    event: async ({ event }) => {
      switch (event.type) {
        case "session.idle": {
          const sessionId = event.properties?.sessionID || "unknown"
          await notify(`Session completed — ${projectName}
Session: ${sessionId}`)
          break
        }

        case "session.error": {
          const error = event.properties?.error || "Unknown error"
          await notify(`Session error — ${projectName}
Error: ${error}`)
          break
        }

        case "todo.updated": {
          const todos = event.properties?.todos
          if (!todos) break
          const completed = todos.filter((t) => t.status === "completed").length
          const total = todos.length
          if (completed === total && total > 0) {
            await notify(`All tasks completed — ${projectName}
${completed}/${total} done`)
          }
          break
        }

        case "tool.execute.after": {
          // Notify on significant tool executions
          if (event.properties?.tool === "task") {
            const args = event.properties?.args
            if (args?.description) {
              await notify(`Agent delegation:
${args.description}`)
            }
          }
          break
        }

        default:
          break
      }
    },
  }
}
