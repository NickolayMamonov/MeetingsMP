# OpenClaw Gateway Launcher для MeetingsMP
# Положи токен в эту же папку или введи его ниже

$DISCORD_TOKEN = "ВСТАВЬ_СЮДА_ТОКЕН_БОТА"

# Если токен не вставлен прямо — спросить
if ($DISCORD_TOKEN -eq "ВСТАВЬ_СЮДА_ТОКЕН_БОТА") {
    $DISCORD_TOKEN = Read-Host "Введи Discord Bot Token"
}

if (-not $DISCORD_TOKEN) {
    Write-Host "Токен не указан. Выход." -ForegroundColor Red
    exit 1
}

$env:DISCORD_BOT_TOKEN = $DISCORD_TOKEN

Write-Host "=== Запуск OpenClaw Gateway ===" -ForegroundColor Cyan
Write-Host "Токен: $($DISCORD_TOKEN.Substring(0,10))..." -ForegroundColor DarkGray
Write-Host "Дашборд: http://127.0.0.1:18789" -ForegroundColor Green
Write-Host ""

# Запуск gateway
openclaw gateway start
