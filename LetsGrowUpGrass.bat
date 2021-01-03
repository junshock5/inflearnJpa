@echo off

:LetsGrowUpGrass
git add .
git commit -m "Planted grass"
git push -u origin +master > pushlog.txt
set /p writePushLog=<pushlog.txt
GOTO CheckGithubUpload

:CheckGithubUpload
set /p readPushLog=<pushlog.txt
REM 깃허브 업로드가 안되었을 경우(인터넷 문제 등) 계속해서 업로드 요청함.
IF "%readPushLog%"=="" (
  timeout 5
  GOTO LetsGrowUpGrass
REM 깃허브 업로드가 성공적으로 수행되었을 경우 로그를 남김.
) ELSE (
  echo [%DATE% %TIME:~0,8%] 잔디를 심었습니다. >> log.txt
)

GOTO LetGrowUpgrass