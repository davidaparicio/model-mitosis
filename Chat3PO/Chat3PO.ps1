Add-Type -AssemblyName presentationCore
$player = New-Object System.Windows.Media.MediaPlayer

Set-Location C:\Users\Julien\IdeaProjects\model-mitosis\backend
Clear-Host
function Typing($sentence) {

    Write-Host "Chat3PO> " -NoNewLine -ForegroundColor Yellow
    foreach ($char in $sentence.ToCharArray()) {
        Write-Host -NoNewline $char
        Start-Sleep -Milliseconds 50
    }
    Write-Host
}

function EverythingInSearchDomain() {

    Write-Host "Chat3PO> " -NoNewLine -ForegroundColor Yellow
    $part1="My goodness! The task was not without its challenges. The very software upon which we relied, Intellij, "
    $part2="was most uncooperative, causing no small amount of frustration and delay"
    foreach ($char in $part1.ToCharArray()) {
        Write-Host -NoNewline $char
        Start-Sleep -Milliseconds 50
    }
    Start-Sleep -Milliseconds 300
    foreach ($char in $part2.ToCharArray()) {
        Write-Host -NoNewline $char
        Start-Sleep -Milliseconds 50
    }
    Write-Host
}

$sound=[uri]"C:\Users\Julien\IdeaProjects\model-mitosis\Chat3PO\hello-master.mp3"
$player.Open($sound)
$player.Play()
Typing "Hello master, how may I be of service to you today?"

do {
    Write-Host "Chat3PO> " -NoNewLine -ForegroundColor Yellow
    $param = $Host.UI.ReadLine()

    if ($param.ToLower().Contains("everything")) {
        $sound=[uri]"C:\Users\Julien\IdeaProjects\model-mitosis\Chat3PO\of-course.mp3"
        $player.Open($sound)
        $player.Play()
        Typing "Of course! Proceeding immediately"

        $branchName = Get-Date -Format "yyyy-MM-dd-HHmm"
        git checkout -b $branchName ad06049fd4b4826fb20c602e5a8f9f7b64ceb443 2>&1 > $null

        $sound=[uri]"C:\Users\Julien\IdeaProjects\model-mitosis\Chat3PO\everything-in-search-domain.mp3"
        $player.Open($sound)
        $player.Play()
        EverythingInSearchDomain
    }else{
        Write-Host "nothing"
    }

} while ($true)