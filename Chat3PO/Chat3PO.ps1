cd C:\Users\Julien\IdeaProjects\model-mitosis\backend

Clear-Host

do {
    Write-Host "Chat3PO> " -NoNewLine -ForegroundColor Yellow
    $param = $Host.UI.ReadLine()

    if ($param.ToLower().Contains("everything")) {
        $branchName = Get-Date -Format "yyyy-MM-dd-HHmm"
        git checkout -b $branchName 2b26e8a4 2>&1 > $null
        Write-Host "Done !"
    }else{

        Write-Host "nothing"
    }

} while ($true)