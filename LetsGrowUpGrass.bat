@echo off

:LetsGrowUpGrass
git add .
git commit -m "Planted grass"
git push -u origin +main > pushlog.txt
set /p writePushLog=<pushlog.txt
GOTO CheckGithubUpload

:CheckGithubUpload
set /p readPushLog=<pushlog.txt
REM ����� ���ε尡 �ȵǾ��� ���(���ͳ� ���� ��) ����ؼ� ���ε� ��û��.
IF "%readPushLog%"=="" (
  timeout 5
  GOTO LetsGrowUpGrass
REM ����� ���ε尡 ���������� ����Ǿ��� ��� �α׸� ����.
) ELSE (
  echo [%DATE% %TIME:~0,8%] �ܵ� �ɾ����ϴ�. >> log.txt
)

GOTO LetGrowUpgrass