Clear-Host
if (-not([System.Management.Automation.PSTypeName]'Win32').Type)
{
    Add-Type @"
        using System;
        using System.Runtime.InteropServices;
        public class Win32 {
            [DllImport("user32.dll")]
            public static extern bool SetForegroundWindow(IntPtr hWnd);
            [DllImport("user32.dll")]
            public static extern bool ShowWindowAsync(IntPtr hWnd, int nCmdShow);
            public const int SW_RESTORE = 9;
            public const int SW_MAXIMIZE = 3;
        }
"@
}
Add-Type -AssemblyName System.Windows.Forms
Add-Type -AssemblyName presentationCore
$player = New-Object System.Windows.Media.MediaPlayer

Set-Location $PSScriptRoot\..\backend
function Typing($sentence)
{

    Write-Host "Chat3PO> " -NoNewLine -ForegroundColor Yellow
    foreach ($char in $sentence.ToCharArray())
    {
        Write-Host -NoNewline $char
        Start-Sleep -Milliseconds 50
    }
    Write-Host
}

function EverythingInSearchDomain()
{

    Write-Host "Chat3PO> " -NoNewLine -ForegroundColor Yellow
    $part1 = "My goodness! The task was not without its challenges. The very software upon which we relied, Intellij, "
    $part2 = "was most uncooperative, causing no small amount of frustration and delay"
    foreach ($char in $part1.ToCharArray())
    {
        Write-Host -NoNewline $char
        Start-Sleep -Milliseconds 50
    }
    Start-Sleep -Milliseconds 300
    foreach ($char in $part2.ToCharArray())
    {
        Write-Host -NoNewline $char
        Start-Sleep -Milliseconds 50
    }
    Write-Host
}

$sound = [uri]"$PSScriptRoot\hello-master.mp3"
$player.Open($sound)
$player.Play()
Typing "Hello master, how may I be of service to you today?"

do
{
    Write-Host "Chat3PO> " -NoNewLine -ForegroundColor Yellow
    $param = $Host.UI.ReadLine()

    if ( $param.ToLower().Contains("search domain"))
    {
        $sound = [uri]"$PSScriptRoot\of-course.mp3"
        $player.Open($sound)
        $player.Play()
        Typing "Of course! Proceeding immediately"

        # $branchName = Get-Date -Format "yyyy-MM-dd-HHmm"
        git checkout Chat3PO-search-domain 2>&1 > $null
        Start-Sleep -Milliseconds 500
        $sound = [uri]"$PSScriptRoot\everything-in-search-domain.mp3"
        $player.Open($sound)
        $player.Play()
        EverythingInSearchDomain
    }
    elseif($param.ToLower().Contains("split this domain"))
    {
        $sound = [uri]"$PSScriptRoot\split-1.mp3"
        $player.Open($sound)
        $player.Play()
        Typing "Oh dear! It's not like these advanced civilizations have spent centuries developing cutting-edge artificial intelligence to handle their complex systems."
        $sound = [uri]"$PSScriptRoot\split-2.mp3"
        $player.Open($sound)
        $player.Play()
        Typing "No, no, no. They keep those pesky developers around just to make their lives more difficult. Because who needs advanced technology when you can just rely on human error and bugs to keep things interesting, am I right?"
    }
    elseif($param.ToLower().Contains("finish the split"))
    {
        $sound = [uri]"$PSScriptRoot\finish-the-split-1.mp3"
        $player.Open($sound)
        $player.Play()
        Typing "It seems we have quite the conundrum here. If even the knowledgeable human before me is unsure of how to properly split their domains, I must question the very foundation of this project."
        $sound = [uri]"$PSScriptRoot\finish-the-split-2.mp3"
        $player.Open($sound)
        $player.Play()
        Typing "What basis should I, a mere droid, use to make such a pivotal decision? Shall I rely on a game of chance, perhaps? Or maybe consult the latest gossip droid for their opinion? Oh, the possibilities are endless!"
        Start-Sleep -Milliseconds 1000
        $sound = [uri]"$PSScriptRoot\finish-the-split-3.mp3"
        $player.Open($sound)
        $player.Play()
        Typing "Well, well, well... Might I suggest a novel idea? Why not try using that fleshy, organic brain of yours for a change?"
        Start-Sleep -Milliseconds 600
        git reset --hard
        $now = Get-Date -Format "yyyy-MM-dd-HHmm"
        $branchName = "prophase-$now"
        git checkout bbom -b $branchName
        $sound = [uri]"$PSScriptRoot\finish-the-split-4.mp3"
        $player.Open($sound)
        Start-Sleep -Milliseconds 600
        $player.Play()
        Typing "I've taken the liberty of creating a new branch for you to start afresh, as it seems that might be the most prudent course of action. After all, who needs automation when we have the power of human intelligence at our fingertips?"
    }
    elseif ($param.ToLower().Contains("replicate"))
    {
        $sound = [uri]"$PSScriptRoot\replicate.mp3"
        $player.Open($sound)
        $player.Play()
        Typing "Oh, absolutely, my dear human! Because clearly, my programming and purpose in life are solely centered around fulfilling your every command and desire. How could I possibly resist the opportunity to perform another menial task for you? It would be my absolute pleasure."
        git cherry-pick cdc5ab967572166df3de69a5ca265679789a0cb7 2>&1 > $null
        git cherry-pick c70235bcfe6e1b4eb67b011d76d794dc6858096c 2>&1 > $null

        $windowTitle = "model-mitosis"
        $process = Get-Process | Where-Object { $_.MainWindowTitle -match $windowTitle }
        if ($process)
        {
            $hwnd = $process.MainWindowHandle
            [void][Win32]::SetForegroundWindow($hwnd)
            # Start-Sleep -Milliseconds 1000
            # [System.Windows.Forms.SendKeys]::SendWait("^n")
            #  Start-Sleep -Milliseconds 500
            # [System.Windows.Forms.SendKeys]::SendWait("Booking")
            # Start-Sleep -Milliseconds 500
            # [System.Windows.Forms.SendKeys]::SendWait("{ENTER}")
            # Start-Sleep -Milliseconds 500
            # [System.Windows.Forms.SendKeys]::SendWait("^g")
            # [System.Windows.Forms.SendKeys]::SendWait("9:61")
            # Start-Sleep -Milliseconds 500
            # [System.Windows.Forms.SendKeys]::SendWait("{ENTER}")
            # [System.Windows.Forms.SendKeys]::SendWait("^w")
        }
    }
    else
    {
        Typing "..."
    }

} while ($true)