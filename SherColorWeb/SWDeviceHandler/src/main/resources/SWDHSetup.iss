; Script generated by the Inno Setup Script Wizard.
; SEE THE DOCUMENTATION FOR DETAILS ON CREATING INNO SETUP SCRIPT FILES!

#define MyAppName "SWDeviceHandler"
#define MyAppVersion "0.15"
#define MyAppPublisher "The Sherwin-Williams Company"
#define MyAppURL "http://www.sherwin-williams.com/"

[Setup]
; NOTE: The value of AppId uniquely identifies this application.
; Do not use the same AppId value in installers for other applications.
; (To generate a new GUID, click Tools | Generate GUID inside the IDE.)
AppId={{8D52E46D-B3C1-497A-B30A-1AF82D60DDFB}
AppName={#MyAppName}
AppVersion={#MyAppVersion}
;AppVerName={#MyAppName} {#MyAppVersion}
AppPublisher={#MyAppPublisher}
AppPublisherURL={#MyAppURL}
AppSupportURL={#MyAppURL}
AppUpdatesURL={#MyAppURL}
DefaultDirName={pf}\{#MyAppName}
DisableDirPage=yes
DefaultGroupName={#MyAppName}
DisableProgramGroupPage=yes
OutputBaseFilename=SWDHSetup
Compression=lzma
SolidCompression=yes

[Languages]
Name: "english"; MessagesFile: "compiler:Default.isl"

[Dirs]
Name: "{app}\spectro"
Name: "{app}\FMSocketServer"

[Files]
Source: "C:\swdevicehandler\nssm.exe"; DestDir: "{app}"; Flags: ignoreversion
Source: "C:\swdevicehandler\SWDeviceHandler.bat"; DestDir: "{app}"; Flags: ignoreversion
Source: "C:\swdevicehandler\SWDeviceHandler.jar"; DestDir: "{app}"; Flags: ignoreversion
Source: "C:\swdevicehandler\localhost.jks"; DestDir: "{app}"; Flags: ignoreversion
Source: "C:\swdevicehandler\localhost.crt"; DestDir: "{app}"; Flags: ignoreversion
Source: "C:\swdevicehandler\FMSocketServer\FM_CORE_DISPENSER.dll"; DestDir: "{app}\FMSocketServer"; Flags: ignoreversion
Source: "C:\swdevicehandler\FMSocketServer\FM_CORE_DISPENSER.pdb"; DestDir: "{app}\FMSocketServer"; Flags: ignoreversion
Source: "C:\swdevicehandler\FMSocketServer\FM_IDD.dll"; DestDir: "{app}\FMSocketServer"; Flags: ignoreversion
Source: "C:\swdevicehandler\FMSocketServer\FM_IDD.pdb"; DestDir: "{app}\FMSocketServer"; Flags: ignoreversion
Source: "C:\swdevicehandler\FMSocketServer\FM_IDD.xml"; DestDir: "{app}\FMSocketServer"; Flags: ignoreversion
Source: "C:\swdevicehandler\FMSocketServer\FMAsyncSocketServer.exe"; DestDir: "{app}\FMSocketServer"; Flags: ignoreversion
Source: "C:\swdevicehandler\FMSocketServer\FMAsyncSocketServer.exe.config"; DestDir: "{app}\FMSocketServer"; Flags: ignoreversion
Source: "C:\swdevicehandler\FMSocketServer\FMAsyncSocketServer.pdb"; DestDir: "{app}\FMSocketServer"; Flags: ignoreversion
Source: "C:\swdevicehandler\FMSocketServer\FMAsyncSocketServer1.exe.config"; DestDir: "{app}\FMSocketServer"; Flags: ignoreversion
Source: "C:\swdevicehandler\FMSocketServer\log4net.dll"; DestDir: "{app}\FMSocketServer"; Flags: ignoreversion
Source: "C:\swdevicehandler\FMSocketServer\log4net.xml"; DestDir: "{app}\FMSocketServer"; Flags: ignoreversion
Source: "C:\swdevicehandler\FMSocketServer\Newtonsoft.Json.dll"; DestDir: "{app}\FMSocketServer"; Flags: ignoreversion
Source: "C:\swdevicehandler\FMSocketServer\Newtonsoft.Json.xml"; DestDir: "{app}\FMSocketServer"; Flags: ignoreversion
Source: "C:\swdevicehandler\FMSocketServer\nunit.framework.dll"; DestDir: "{app}\FMSocketServer"; Flags: ignoreversion
Source: "C:\swdevicehandler\FMSocketServer\nunit.framework.xml"; DestDir: "{app}\FMSocketServer"; Flags: ignoreversion
Source: "C:\swdevicehandler\FMSocketServer\nunit_random_seed.tmp"; DestDir: "{app}\FMSocketServer"; Flags: ignoreversion
Source: "C:\swdevicehandler\FMSocketServer\SWDeviceHandler-Common.dll"; DestDir: "{app}\FMSocketServer"; Flags: ignoreversion
Source: "C:\swdevicehandler\FMSocketServer\SWDeviceHandler-Common.dll.config"; DestDir: "{app}\FMSocketServer"; Flags: ignoreversion
Source: "C:\swdevicehandler\FMSocketServer\SWDeviceHandler-Common.pdb"; DestDir: "{app}\FMSocketServer"; Flags: ignoreversion
Source: "C:\swdevicehandler\FMSocketServer\SWFluid.dll"; DestDir: "{app}\FMSocketServer"; Flags: ignoreversion
Source: "C:\swdevicehandler\FMSocketServer\SWFluid.dll.config"; DestDir: "{app}\FMSocketServer"; Flags: ignoreversion
Source: "C:\swdevicehandler\FMSocketServer\SWFluid.Net.dll"; DestDir: "{app}\FMSocketServer"; Flags: ignoreversion
Source: "C:\swdevicehandler\FMSocketServer\SWFluid.Net.dll.config"; DestDir: "{app}\FMSocketServer"; Flags: ignoreversion
Source: "C:\swdevicehandler\FMSocketServer\SWFluid.Net.pdb"; DestDir: "{app}\FMSocketServer"; Flags: ignoreversion
Source: "C:\swdevicehandler\FMSocketServer\SWFluid.pdb"; DestDir: "{app}\FMSocketServer"; Flags: ignoreversion

; NOTE: Don't use "Flags: ignoreversion" on any shared system files

[Icons]
Name: "{group}\{cm:UninstallProgram,{#MyAppName}}"; Filename: "{uninstallexe}"

[Run]
Filename: "{app}\nssm.exe"; Parameters: "set SWDeviceHandler Application {app}\SWDeviceHandler.bat"
Filename: "{app}\nssm.exe"; Parameters: "install SWDeviceHandler SWDeviceHandler.bat"
Filename: "{app}\nssm.exe"; Parameters: "set SWDeviceHandler Application {app}\SWDeviceHandler.bat"
Filename: "{app}\nssm.exe"; Parameters: "set SWDeviceHandler AppDirectory {app}"
Filename: "{app}\nssm.exe"; Parameters: "set SWDeviceHandler DisplayName SWDeviceHandler"
Filename: "{app}\nssm.exe"; Parameters: "set SWDeviceHandler Description SW Tinter and Color Eye Service"
Filename: "{app}\nssm.exe"; Parameters: "start SWDeviceHandler"

[UninstallRun]
Filename: "{app}\nssm.exe"; Parameters: "stop SWDeviceHandler"
Filename: "{app}\nssm.exe"; Parameters: "remove SWDeviceHandler"

