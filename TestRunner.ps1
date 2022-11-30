Param (
    [ArgumentCompletions(
        'Initialization',
        'RegisterListener_GenerateLauncher',
        'Verify_RegisterListener_GenerateLauncher',
        'CopyLauncherToDestination',
        'StartGruntConnection',
        'Verify_StartGruntConnection',
        'StopGruntConnection',
        'Verify_StopGruntConnection',
        'Finish'
    )] [string[]] $Lifecycle = @(
        'Initialization',
        'RegisterListener_GenerateLauncher',
        'Verify_RegisterListener_GenerateLauncher',
        'CopyLauncherToDestination',
        'StartGruntConnection',
        'Verify_StartGruntConnection',
        'StopGruntConnection',
        'Verify_StopGruntConnection',
        'Finish'
    ),

    [ArgumentCompletions(
        'Initialization',
        'RegisterListener_GenerateLauncher',
        'Verify_RegisterListener_GenerateLauncher',
        'CopyLauncherToDestination',
        'StartGruntConnection',
        'Verify_StartGruntConnection',
        'StopGruntConnection',
        'Verify_StopGruntConnection',
        'Finish'
    )] [string] $StopOnLifecycle = "",

    [string] $WinHostName = "",
    [string] $WinUname = "",
    [string] $WinPword = "",
    [string] $WinIP = "",

    [string] $HostIP = "",
    [string] $CovenantHost = "",
    [string] $CovenantUname = "",
    [string] $CovenantPword = "",
    [string] $ConfigFile = "./config/config.json",

    [string] $ReportFolder = ""
)

[object] $Lifecycles = @{
    INITIALIZATION = 'Initialization'
    REGISTER_LISTENER_GENERATE_LAUNCHER = 'RegisterListener_GenerateLauncher'
    VERIFY_REGISTER_LISTENER_GENERATE_LAUNCHER = 'Verify_RegisterListener_GenerateLauncher'
    COPY_LAUNCHER_TO_DESTINATION = 'CopyLauncherToDestination'
    START_GRUNT_CONNECTION = 'StartGruntConnection'
    VERIFY_START_GRUNT_CONNECTION = 'Verify_StartGruntConnection'
    STOP_GRUNT_CONNECTION = 'StopGruntConnection'
    VERIFY_STOP_GRUNT_CONNECTION = 'Verify_StopGruntConnection'
    FINISH = 'Finish'
}

[string[]] $script:Lifecycle = $Lifecycle

if ([string]::IsNullOrEmpty($env:PROJECT_ROOT)) {
    $env:PROJECT_ROOT = ${pwd}
}

#-----------------------------
# Define inputs
# Read 
#-----------------------------

if (Test-Path -Path $ConfigFile) {
    $config = (Get-Content -Path $ConfigFile | ConvertFrom-Json)
    $WinHostName = if ([string]::IsNullOrEmpty($WinHostName)) {$config.WinHostName}
    $WinUname = if ([string]::IsNullOrEmpty($WinUname)) {$config.WinUname}
    $WinPword = if ([string]::IsNullOrEmpty($WinPword)) {$config.WinPword}
    $WinIP = if ([string]::IsNullOrEmpty($WinIP)) {$config.WinIP}
    $HostIP = if ([string]::IsNullOrEmpty($HostIP)) {$config.HostIP}
    $CovenantHost = if ([string]::IsNullOrEmpty($CovenantHost)) {$config.CovenantHost}
    $CovenantUname = if ([string]::IsNullOrEmpty($CovenantUname)) {$config.CovenantUname}
    $CovenantPword = if ([string]::IsNullOrEmpty($CovenantPword)) {$config.CovenantPword}
    $ReportFolder = if ([string]::IsNullOrEmpty($ReportFolder)) {$config.ReportFolder} else {'./report'}
}

$ScreenshotsFolder = "$ReportFolder/screenshots"
$LauncherFolder = "$ReportFolder/launcher"
$ListenerFolder = "$ReportFolder/listener"
$ListenerNameFile = "$ListenerFolder/name"
$OutputFile = "$ReportFolder/output.log"

Write-Host -ForegroundColor Gray "Lifecycle: $Lifecycle"
Write-Host -ForegroundColor Gray "WinHostName: $WinHostName"
Write-Host -ForegroundColor Gray "WinUname: $WinUname"
Write-Host -ForegroundColor Gray "WinPword: $WinPword"
Write-Host -ForegroundColor Gray "WinIP: $WinIP"
Write-Host -ForegroundColor Gray "HostIP: $HostIP"
Write-Host -ForegroundColor Gray "CovenantHost: $CovenantHost"
Write-Host -ForegroundColor Gray "CovenantUname: $CovenantUname"
Write-Host -ForegroundColor Gray "CovenantPword: $CovenantPword"
Write-Host -ForegroundColor Gray "ReportFolder: $ReportFolder"
Write-Host -ForegroundColor Gray "ScreenshotsFolder: $ScreenshotsFolder"
Write-Host -ForegroundColor Gray "LauncherFolder: $LauncherFolder"
Write-Host -ForegroundColor Gray "ListenerFolder: $ListenerFolder"

if ($script:Lifecycle[0].contains(",")) {
    $script:Lifecycle = $script:Lifecycle -split(",")
}

$MavenCache = './m2'
$PlaywrightCache = '.playwright-cache'


#-----------------------------
# Functions
#-----------------------------

function PrepareFolder() {
    param([String] $FolderName, [switch] $Erase)
    Write-Host -ForegroundColor Gray "Preparing folder: '$FolderName'. Erase: $Erase"

    if (Test-Path -Path $FolderName) {
        if ($Erase) {
            Remove-Item -Path "$FolderName/*" -Force -Recurse
        }
    } else {
        New-Item -Path $FolderName -Type Directory | Out-Null
    }
}


[object] $script:WinRemoteSession

function EnsureDestinationConnection() {
    if ([string]::IsNullOrEmpty($script:WinRemoteSession) -Or $script:WinRemoteSession.State -ne 'Opened') {
        Write-Host -ForegroundColor Blue "No Active connection to destination is found. Connecting!"
        $WinFullUserName = "$WinHostName\$WinUname"
        $WinSecWord = ConvertTo-SecureString -String $WinPword -AsPlainText -Force
        $WinCreds = New-Object -TypeName System.Management.Automation.PSCredential -ArgumentList $WinFullUserName, $WinSecWord
        $script:WinRemoteSession = New-PSSession -ComputerName $WinIP -Credential $WinCreds -Authentication Negotiate
    }

    Write-Host -ForegroundColor Gray ("WinRemoteSession.State = {0}" -f $script:WinRemoteSession.State)

    if ($script:WinRemoteSession.State -eq 'Opened') {
        Write-Host -ForegroundColor Yellow "Successfully connected to $WinIP"
    } else {
        Write-Error "Unable to connect to host $WinIP!"
        $script:Lifecycle = @()
    }
}

function ReportStartLifecycle() {
    [Parameter(Mandatory = $true)]
    param([string] $lc)
    Write-Host -ForegroundColor DarkYellow "`nStarting Lifecycle: $lc`n"
}

function ReportStopOnLifecycle() {
    [Parameter(Mandatory = $true)]
    param([string] $lc)
    Write-Host -ForegroundColor DarkYellow "`nStopping on Lifecycle: $lc!`n"
}

function RunUITest() {
    [Parameter(Mandatory = $true)]
    param([string[]] $mvn_args)
    docker run --rm -it -v $env:PROJECT_ROOT/:/tests -v $env:PROJECT_ROOT/.m2:/root/.m2 --ipc=host covenant-pw-java:v1 mvn clean install $mvn_args
}



#---------------------------------------------------------------------------------------
# Main
#---------------------------------------------------------------------------------------

if (Test-Path -Path $OutputFile) {
    Remove-Item -Path $OutputFile
}

Start-Transcript -Path $OutputFile 

#-----------------------------
# Cleanup and prepare report directory
#-----------------------------

$CurrentLifecycle = $Lifecycles.INITIALIZATION

if ($StopOnLifecycle -eq $CurrentLifecycle) {
    ReportStopOnLifecycle $CurrentLifecycle
    $script:Lifecycle = @()
}

if ($script:Lifecycle -contains $CurrentLifecycle) {
    ReportStartLifecycle $CurrentLifecycle

    PrepareFolder $MavenCache
    PrepareFolder $PlaywrightCache

    PrepareFolder $ReportFolder
    PrepareFolder $ScreenshotsFolder -Erase
}


#-----------------------------
# Run Launcher creator test
#-----------------------------

$CurrentLifecycle = $Lifecycles.REGISTER_LISTENER_GENERATE_LAUNCHER

if ($StopOnLifecycle -eq $CurrentLifecycle) {
    ReportStopOnLifecycle $CurrentLifecycle
    $script:Lifecycle = @()
}

if ($script:Lifecycle -contains $CurrentLifecycle) {
    ReportStartLifecycle $CurrentLifecycle

    PrepareFolder $LauncherFolder -Erase
    PrepareFolder $ListenerFolder -Erase

    $mvn_args = @()
    $mvn_args += ("-D{0}={1}" -f 'test', 'CreateListenerLauncherTest')
    $mvn_args += ("-D{0}={1}" -f 'CovenantHost', $CovenantHost)
    $mvn_args += ("-D{0}={1}" -f 'CovenantUname', $CovenantUname)
    $mvn_args += ("-D{0}={1}" -f 'CovenantPword', $CovenantPword)
    $mvn_args += ("-D{0}={1}" -f 'ScreenshotsFolder', $ScreenshotsFolder)
    $mvn_args += ("-D{0}={1}" -f 'LauncherFolder', $LauncherFolder)
    $mvn_args += ("-D{0}={1}" -f 'ListenerFolder', $ListenerFolder)
    $mvn_args += ("-D{0}={1}" -f 'ListenerNameFile', $ListenerNameFile)
    $mvn_args += ("-D{0}={1}" -f 'HostIP', $HostIP)

    RunUITest $mvn_args
}


#-----------------------------
# Run Launcher creator test
#-----------------------------

$CurrentLifecycle = $Lifecycles.VERIFY_REGISTER_LISTENER_GENERATE_LAUNCHER

if ($StopOnLifecycle -eq $CurrentLifecycle) {
    ReportStopOnLifecycle $CurrentLifecycle
    $script:Lifecycle = @()
}

if ($script:Lifecycle -contains $CurrentLifecycle) {
    ReportStartLifecycle $CurrentLifecycle

    if (-Not (Test-Path -Path $LauncherFolder/*)) {
        throw "No Launcher was found in $LauncherFolder"
    } else {
        Write-Host -ForegroundColor Yellow "Following Launchers are found: " -NoNewline
        Get-ChildItem -Path $LauncherFolder/*
    }
}

#-----------------------------
# Copy Launcher and run on Win
#-----------------------------

[string] $script:LauncherDestinationPath
[string] $script:LauncherName

$CurrentLifecycle = $Lifecycles.COPY_LAUNCHER_TO_DESTINATION

if ($StopOnLifecycle -eq $CurrentLifecycle) {
    ReportStopOnLifecycle $CurrentLifecycle
    $script:Lifecycle = @()
}

if ($script:Lifecycle -contains $CurrentLifecycle) {
    ReportStartLifecycle $CurrentLifecycle

    EnsureDestinationConnection

    Write-Host 'After connect ' $script:WinRemoteSession.State

    Write-Host 'Taking the first Launcher'
    [object] $Launcher = Get-ChildItem -Path $LauncherFolder/* | Select-Object -First 1

    $script:LauncherName = $Launcher.Name
    [string] $LauncherFullPath = $Launcher.ToString()
    $script:LauncherDestinationPath = "C:\Users\$WinUname\AppData\Local\Temp\$LauncherName"

    Write-Host -ForegroundColor Gray ('Launcher name: {0}' -f $script:LauncherName)
    Write-Host -ForegroundColor Gray ('Launcher full path: {0}' -f $script:LauncherFullPath)
    Write-Host -ForegroundColor Gray ('Launcher destination path: {0}' -f $script:LauncherDestinationPath)
    Write-Host -ForegroundColor DarkBlue ('Copying {0} to {1}' -f $LauncherFullPath, $script:LauncherDestinationPath)

    Copy-Item -ToSession $script:WinRemoteSession $LauncherFullPath -Destination $script:LauncherDestinationPath


    #--------------------------------------------------------
    # Alternative we can try ssh connection and Expect scripting methods
    #--------------------------------------------------------
    # scp $LauncherFileName $WinUname@$WinIP:$LauncherFullPath
}


#-----------------------------
# Start Launcher process on destination
#-----------------------------

$CurrentLifecycle = $Lifecycles.START_GRUNT_CONNECTION

if ($StopOnLifecycle -eq $CurrentLifecycle) {
    ReportStopOnLifecycle $CurrentLifecycle
    $script:Lifecycle = @()
}

if ($script:Lifecycle -contains $CurrentLifecycle) {
    ReportStartLifecycle $CurrentLifecycle
    EnsureDestinationConnection

    Invoke-Command -Session $script:WinRemoteSession -ScriptBlock {
        param([string] $arg )
        Write-Host -ForegroundColor Magenta "[Destination] Starting new process $arg"
        Start-Process -NoNewWindow -FilePath $arg
    } -ArgumentList $script:LauncherDestinationPath

    Start-Sleep -Seconds 3

    Invoke-Command -Session $script:WinRemoteSession -ScriptBlock {
        param([string] $arg )
        Write-Host -ForegroundColor Magenta "[Destination] Parsing new process $arg"
        ps | Where-Object ProcessName -EQ $arg | Out-Host
    } -ArgumentList $script:LauncherName

    #--------------------------------------------------------
    # Alternative we can try ssh and Expect scripting methods
    #--------------------------------------------------------
}


#-----------------------------
# Verify Grant exists and is active
#-----------------------------

$CurrentLifecycle = $Lifecycles.VERIFY_START_GRUNT_CONNECTION

if ($StopOnLifecycle -eq $CurrentLifecycle) {
    ReportStopOnLifecycle $CurrentLifecycle
    $script:Lifecycle = @()
}

if ($script:Lifecycle -contains $CurrentLifecycle) {
    ReportStartLifecycle $CurrentLifecycle

    $mvn_args = @()
    $mvn_args += ("-D{0}={1}" -f 'test', 'VerifyGruntStartedTest')
    $mvn_args += ("-D{0}={1}" -f 'CovenantHost', $CovenantHost)
    $mvn_args += ("-D{0}={1}" -f 'CovenantUname', $CovenantUname)
    $mvn_args += ("-D{0}={1}" -f 'CovenantPword', $CovenantPword)
    $mvn_args += ("-D{0}={1}" -f 'ScreenshotsFolder', $ScreenshotsFolder)
    $mvn_args += ("-D{0}={1}" -f 'LauncherFolder', $LauncherFolder)
    $mvn_args += ("-D{0}={1}" -f 'HostIP', $HostIP)
    $mvn_args += ("-D{0}={1}" -f 'WinHostName', $WinHostName)
    $mvn_args += ("-D{0}={1}" -f 'WinUname', $WinUname)

    RunUITest $mvn_args
}


#-----------------------------
# Stop Launcher process on destination
#-----------------------------

$CurrentLifecycle = $Lifecycles.STOP_GRUNT_CONNECTION

if ($StopOnLifecycle -eq $CurrentLifecycle) {
    ReportStopOnLifecycle $CurrentLifecycle
    $script:Lifecycle = @()
}

if ($script:Lifecycle -contains $CurrentLifecycle) {
    ReportStartLifecycle $CurrentLifecycle
    EnsureDestinationConnection

    Invoke-Command -Session $script:WinRemoteSession -ScriptBlock {
        param([string] $arg )
        Write-Host -ForegroundColor Magenta "[Destination] Stopping the Grunt process $arg"
        Stop-Process -Name $arg
    } -ArgumentList ($script:LauncherName.trim(".exe"))

    Start-Sleep -Seconds 3

    #--------------------------------------------------------
    # Alternative we can try ssh and Expect scripting methods
    #--------------------------------------------------------
}


#-----------------------------
# Verify Grant connection has lost
#-----------------------------

$CurrentLifecycle = $Lifecycles.VERIFY_STOP_GRUNT_CONNECTION

if ($StopOnLifecycle -eq $CurrentLifecycle) {
    ReportStopOnLifecycle $CurrentLifecycle
    $script:Lifecycle = @()
}

if ($script:Lifecycle -contains $CurrentLifecycle) {
    ReportStartLifecycle $CurrentLifecycle

    $mvn_args = @()
    $mvn_args += ("-D{0}={1}" -f 'test', 'VerifyGruntStoppedTest')
    $mvn_args += ("-D{0}={1}" -f 'CovenantHost', $CovenantHost)
    $mvn_args += ("-D{0}={1}" -f 'CovenantUname', $CovenantUname)
    $mvn_args += ("-D{0}={1}" -f 'CovenantPword', $CovenantPword)
    $mvn_args += ("-D{0}={1}" -f 'ScreenshotsFolder', $ScreenshotsFolder)
    $mvn_args += ("-D{0}={1}" -f 'LauncherFolder', $LauncherFolder)
    $mvn_args += ("-D{0}={1}" -f 'HostIP', $HostIP)
    $mvn_args += ("-D{0}={1}" -f 'WinHostName', $WinHostName)
    $mvn_args += ("-D{0}={1}" -f 'WinUname', $WinUname)

    RunUITest $mvn_args
}



#-----------------------------
# Remove the Listener
#-----------------------------

$CurrentLifecycle = $Lifecycles.FINISH

if ($script:Lifecycle -contains $CurrentLifecycle) {
    ReportStartLifecycle $CurrentLifecycle

    [bool] $IsListenerReportFileFound = (Test-Path -Path $ListenerNameFile)
    [bool] $IsListenerNameProvided = $false

    if ($IsListenerReportFileFound) {
        $ListenerName = Get-Content $ListenerNameFile
        $IsListenerNameProvided = -Not [string]::IsNullOrEmpty($ListenerName)
    }

    if ($IsListenerNameProvided) {
        $mvn_args = @()
        $mvn_args += ("-D{0}={1}" -f 'test', 'DeleteListenerTest')
        $mvn_args += ("-D{0}={1}" -f 'CovenantHost', $CovenantHost)
        $mvn_args += ("-D{0}={1}" -f 'CovenantUname', $CovenantUname)
        $mvn_args += ("-D{0}={1}" -f 'CovenantPword', $CovenantPword)
        $mvn_args += ("-D{0}={1}" -f 'ScreenshotsFolder', $ScreenshotsFolder)
        $mvn_args += ("-D{0}={1}" -f 'LauncherFolder', $LauncherFolder)
        $mvn_args += ("-D{0}={1}" -f 'ListenerName', $ListenerName)
        RunUITest $mvn_args
    } else {
        Write-Warning "Unable to find the listener name to stop and delete it"
    }
}


#-----------------------------
# Close connection with destination
#-----------------------------

if ($script:WinRemoteSession.State -eq 'Opened') {
    Write-Host -ForegroundColor Gray "Closing connection with destination $WinIP!"
    Remove-PSSession $script:WinRemoteSession
}

Stop-Transcript
