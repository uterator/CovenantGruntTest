# This document is not DELIVERABLE

It contains notes from the process of solution.
There could be meaningless statements.

## Windows Defender

Turn **off** the `Real-Time Protection`


# SSH Setup

On Windows Machine the SSH Serviver is installed by default but it is not listening the port

http://woshub.com/connect-to-windows-via-ssh/

To check if the port is LISTENING 

```pwsh
netstat -na
```

if the :22 port is not listed under TPC protocol list, this means the ssh server's service is not running. Start the service by 

```pwsh
Start-Service sshd
```

https://learn.microsoft.com/en-us/windows-server/administration/openssh/openssh_keymanagement

Generate Ed25519 key on the host machine
```bash
ssh-keygen -t ed25519 
```

name and location I set `/Users/gegham/.ssh/id_win10_ed25519`

Load your key files into ssh-agent
```bash
ssh-add /Users/gegham/.ssh/id_win10_ed25519
```

Deploy public key to Windows 10

```pwsh
scp /Users/gegham/.ssh/id_win10_ed25519.pub IEUser@192.168.0.104:/C:/Users/IEUser/Desktop/id_win10_ed25519.pub
```

Add public key on destination Win 10 machine
```pwsh
Add-Content C:\Users\IEUser\.ssh\authorized_keys (Get-Content C:\Users\IEUser\Desktop\id_win10_ed25519.pub)
```

Allow ssh connection throught public/private keys

Chagne in `"C:\ProgramData\ssh\sshd_config"` file following lines
```bash
StrictModes no # to not check if the passphrase is empty
PubkeyAuthentication yes # allow pubkey authentication
```

Copy keys from host to client
```bash
ssh-copy-id -i /Users/gegham/.ssh/id_win10_ed25519 IEUser@192.168.0.104
```

## SSH Issue

https://github.com/PowerShell/Win32-OpenSSH/wiki/Troubleshooting-Steps

While trying to connect to windows via ssh it was always asking for a password.

Was solved by commenting next 2 lines in ssh_config
```
# Match Group administrators
#       AuthorizedKeysFile __PROGRAMDATA__/ssh/administrators_authorized_keys
```

File `$env:ProgramData\ssh\sshd_config`

```pwsh
# This is the sshd server system-wide configuration file.  See
# sshd_config(5) for more information.

# The strategy used for options in the default sshd_config shipped with
# OpenSSH is to specify options with their default value where
# possible, but leave them commented.  Uncommented options override the
# default value.

#Port 22
#AddressFamily any
#ListenAddress 0.0.0.0
#ListenAddress ::

#HostKey __PROGRAMDATA__/ssh/ssh_host_rsa_key
#HostKey __PROGRAMDATA__/ssh/ssh_host_dsa_key
#HostKey __PROGRAMDATA__/ssh/ssh_host_ecdsa_key
#HostKey __PROGRAMDATA__/ssh/ssh_host_ed25519_key

# Ciphers and keying
#RekeyLimit default none

# Logging
# SyslogFacility LOCAL0
# LogLevel DEBUG3

# Authentication:

#LoginGraceTime 2m
#PermitRootLogin prohibit-password
StrictModes no
#MaxAuthTries 6
#MaxSessions 10

PubkeyAuthentication yes

# The default is to check both .ssh/authorized_keys and .ssh/authorized_keys2
# but this is overridden so installations will only check .ssh/authorized_keys
AuthorizedKeysFile	.ssh/authorized_keys

#AuthorizedPrincipalsFile none

# For this to work you will also need host keys in %programData%/ssh/ssh_known_hosts
#HostbasedAuthentication no
# Change to yes if you don't trust ~/.ssh/known_hosts for
# HostbasedAuthentication
#IgnoreUserKnownHosts no
# Don't read the user's ~/.rhosts and ~/.shosts files
#IgnoreRhosts yes

# To disable tunneled clear text passwords, change to no here!

PasswordAuthentication yes

#PermitEmptyPasswords no

#AllowAgentForwarding yes
#AllowTcpForwarding yes
#GatewayPorts no
#PermitTTY yes
#PrintMotd yes
#PrintLastLog yes
#TCPKeepAlive yes
#UseLogin no
#PermitUserEnvironment no
#ClientAliveInterval 0
#ClientAliveCountMax 3
#UseDNS no
#PidFile /var/run/sshd.pid
#MaxStartups 10:30:100
#PermitTunnel no
#ChrootDirectory none
#VersionAddendum none

# no default banner path
#Banner none

# override default of no subsystems
Subsystem	sftp	sftp-server.exe

# New line
Subsystem powershell c:/progra~1/powershell/7/pwsh.exe -sshs -nologo

# Example of overriding settings on a per-user basis
#Match User anoncvs
#	AllowTcpForwarding no
#	PermitTTY no
#	ForceCommand cvs server

# Match Group administrators
#       AuthorizedKeysFile __PROGRAMDATA__/ssh/administrators_authorized_keys
```

## Manual execution:

```powershell
ssh IEUser@192.168.0.104 powershell -Command \"Start-Process -NoNewWindow C:\Users\IEUser\Desktop\GruntHTTP.exe\"
Start-Process -NoNewWindow C:\Users\IEUser\Desktop\GruntHTTP.exe
Stop-Process -Name GruntHTTP
```

Check on windows if the process is running

```powershell
ps | ? ProcessName -Match Gru*
```



# Powershell Remote commands

Start Powershell with admin rights on host

`Install-Module -Name PSWSMan`

`Install-WSMan`

Invoke-Command -ComputerName 192.168.0.104 -ScriptBlock {pwd}


## Client
On client make sure the network type is Private 

`Enable-PSRemoting`

Should be enabled by default

Credentials

`$cred = Get-Credential -UserName IEUser`


## Using Docker

```powershell
docker pull mcr.microsoft.com/powershell
docker run -it mcr.microsoft.com/powershell


Enter-PSSession -ComputerName 192.168.0.104 -Credential $cred -Authentication Digest
Invoke-Command -ComputerName 192.168.0.104 -ScriptBlock {pwd} -Credential $cred
```

## Trablishuting

### ssh-agnet start on container pwsh

```powershell
ssh-agent -s                       
SSH_AUTH_SOCK=/tmp/ssh-XXXXXXO5W6WC/agent.97; export SSH_AUTH_SOCK;
SSH_AGENT_PID=98; export SSH_AGENT_PID;
echo Agent pid 98;
$env:SSH_AGENT_PID = 98
$env:SSH_AUTH_SOCK = '/tmp/ssh-XXXXXXO5W6WC/agent.97'
```

`ssh-add /root/.ssh/id_win10_ed25519`


# PowerShell Remoting from Linux to Windows

https://blog.quickbreach.io/blog/powershell-remoting-from-linux-to-windows/
https://hub.docker.com/r/quickbreach/powershell-ntlm/

```powershell
docker pull quickbreach/powershell-ntlm     # Powershell 6.1.1, supports NTLM
docker run -it quickbreach/powershell-ntlm

$cred = Get-Credential -UserName MSEDGEWIN10\IEUser

Enter-PSSession -ComputerName 192.168.0.104 -Credential $cred -Authentication Negotiate
```

> PowerShell remoting requires Kerberos mutual authentication, which means that the client machine and target machine must both be connected to the domain. That can pose an issue for us testers if we don’t already have a compromised domain-connected box to perform the remoting from. Fortunately, we have the option to add ourselves as a “TrustedHost” in the target’s configuration which will permit us to perform NTLM authentication rather than Kerberos and thus removes the need to connect from a system on the domain.

However, the NTLM connection was [added back (gss-ntlmssp)](https://github.com/PowerShell/PowerShell-Docker/issues/124) to the linux pwsh images, but the ntlm doesn't work with in [pwsh starting v6.2.2](https://github.com/PowerShell/PowerShell/issues/10194)

---

# 20 Nov Summary

## What I'v learned
* From Linux to Windows PWSH Remote connection is possible by NTLM (Negotiate).
* Kerberos works with Windowses in Domain.
* Last version supporting NTLM is Powershell 6.1.1.
* Win11 has a frozzing issue in VBox.

## What I can do
* Passwordless connect from Linux to Windows via `SSH` (OpenSSL)
* Passwordless connect from Linux to Windows via `Powershell Remote` (v6.1.1)

## What I could
* Connect Covenant Launcher to the listener
  * PWSH Listener worked
  * Binary Listener worked
  * Command and Control worked (C2), I run WhiAmI on Covenant host and got an answer from client Win10

## Next

* Try Rowershell Remote with SSH
* Start building Web project with 


### Java project plan

1. Implement the Java project with Playwright java.
2. Control everything with Powershell script.
3. Accept IPs and credentials as arguments to powershell script.
4. Create an image with that project.
   
# Flow Parts:

Part 1: UI project. Prepare Covenant Listener and Launcher **(Java)**

1. Login to Covenant
2. Create a Launcher
3. Create a binnary launcher
4. Download the binary Launcher

Part 2: Copy the launcher and run grunt. **(PowerShell)**

5. Copy the launcher to the client machine
6. Ssh on client machine
7. Run the launcher in background

Part 3: UI project. Verify Grunt is running **(Java)**

8. Go to Covenant UI and find the new Grunt
9.  Check the Grant Status is `Active`
10. Open the Grant and check data

Part 4: UI project. Run C2 command and check the connection works **(Java)**

11. Open the Interact tab
12. Execute a tas `WhoAmI`
13. Wait unti the text appears `WhoAmI completed`
14. Take the `MSEDGEWIN10\IEUser` response

Part 5: Connect to Client and stop the Grunt. **(PowerShell)** 

15. Got to ssh session and kill the Grunt.
16. Go to Covenant UI and find the Grunt.

Part 6: UI project. Check the Grant has been disconnected. **(Java)**

17. Check the Grant Status is `Lost`
18. stop the ssh session
19. Exit the test.

Part 7: Report the status. **(PowerShell)** 



# 26 Nov Summary

Powershell commands
```powershell

$User = "MSEDGEWIN10\IEUser"
$PWord = ConvertTo-SecureString -String "Passw0rd!" -AsPlainText -Force

$creds = $Credential = New-Object -TypeName System.Management.Automation.PSCredential -ArgumentList $User, $PWord

$winss = New-PSSession -ComputerName 192.168.0.104 -Credential $creds -Authentication Negotiate
Copy-Item -ToSession $ss ./taxi -Destination "C:\Users\IEUser\AppData\Local\Temp\taxt2"

Invoke-Command -ComputerName 192.168.0.104 -Credential $cred -Authentication Negotiate -ScriptBlock{ps | ? ProcessName -Match Gru*}

Remove-PSSession $winss
```


# 27 Nov

## Docker build

### Build Powershell image
```powershell
docker build --pull --rm -f "./docker/Dockerfile-pwsh" -t covenant-pwsh-java:v1 ./docker
```

### Build Playwright image for tests
```powershell
docker build --pull --rm -f "./docker/Dockerfile-playwright" -t covenant-pw-java:v1 ./docker
```

## Docker run


### Run tests
```powershell
docker run --rm -it -v /var/run/docker.sock:/var/run/docker.sock -e PROJECT_ROOT=`pwd` -v `pwd`:/runner covenant-pwsh-java:v1 ./TestRunner.ps1

# Example of lifecycles
docker run --rm -it -v /var/run/docker.sock:/var/run/docker.sock -e PROJECT_ROOT=`pwd` -v `pwd`:/runner covenant-pwsh-java:v1 ./TestRunner.ps1 -Lifecycle CopyLauncherToDestination,StartGruntConnection
```

### Run tests
```powershell
docker run --rm -it -v `pwd`:/tests -v `pwd`/.m2:/root/.m2 --ipc=host covenant-pw-java:v1 mvn clean install -Dtest='CreateListenerLauncherTests'
```


# Preperation

1. Windows - stop Real-time protection
2. Make sure there are no active Covenant listeners on port 80


Steps to run

1. Build docker images 
2. For interactive execution run the container and attach to it
    ```powershell
    docker run --rm -it -v /var/run/docker.sock:/var/run/docker.sock -e PROJECT_ROOT=`pwd` -v `pwd`:/runner covenant-pwsh-java:v1
    ```
3. 