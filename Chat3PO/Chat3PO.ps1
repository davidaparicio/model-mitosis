Add-Type -AssemblyName presentationCore
$player = New-Object System.Windows.Media.MediaPlayer

Set-Location $PSScriptRoot\..\backend
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

$sound=[uri]"$PSScriptRoot\hello-master.mp3"
$player.Open($sound)
$player.Play()
Typing "Hello master, how may I be of service to you today?"

do {
    Write-Host "Chat3PO> " -NoNewLine -ForegroundColor Yellow
    $param = $Host.UI.ReadLine()

    if ($param.ToLower().Contains("search domain")) {
        $sound=[uri]"$PSScriptRoot\of-course.mp3"
        $player.Open($sound)
        $player.Play()
        Typing "Of course! Proceeding immediately"

        git checkout Chat3PO-search-domain 2>&1 > $null
        Start-Sleep -Milliseconds 500
        $sound=[uri]"$PSScriptRoot\everything-in-search-domain.mp3"
        $player.Open($sound)
        $player.Play()
        EverythingInSearchDomain
    }elseif($param.ToLower().Contains("split this domain")){
        $sound=[uri]"$PSScriptRoot\split-1.mp3"
        $player.Open($sound)
        $player.Play()
        Typing "Oh dear! It's not like these advanced civilizations have spent centuries developing cutting-edge artificial intelligence to handle their complex systems."
        $sound=[uri]"$PSScriptRoot\split-2.mp3"
        $player.Open($sound)
        $player.Play()
        Typing "No, no, no. They keep those pesky developers around just to make their lives more difficult. Because who needs advanced technology when you can just rely on human error and bugs to keep things interesting, am I right?"
    }elseif($param.ToLower().Contains("finish the split")){
        git reset --hard
        $now = Get-Date -Format "yyyy-MM-dd-HHmm"
        $branchName = "prophase-$now"
        git checkout bbom -b $branchName
    } else{
       Typing "..."
    }

} while ($true)