@echo off
set datesuffix=%date:~-4,4%%date:~-7,2%%date:~-10,2%
set timesuffix=%time:~0,2%%time:~3,2%%time:~6,2%

:M001
if NOT exist H:\RETAIL\eCHEQUE\001\OK.FLG goto M003
rfrompcb C:\BCM\eCheque\TRScript\001\file_upload.dtt
ren H:\RETAIL\eCHEQUE\001\OK.FLG OK.FLG%datesuffix%%timesuffix%
move H:\RETAIL\eCHEQUE\001\OK.FLG%datesuffix%%timesuffix% H:\RETAIL\eCHEQUE\001\bk
ren H:\RETAIL\eCHEQUE\001\chequef.csv chequef.csv%datesuffix%%timesuffix%
move H:\RETAIL\eCHEQUE\001\chequef.csv%datesuffix%%timesuffix% H:\RETAIL\eCHEQUE\001\bk

:M003
if NOT exist H:\RETAIL\eCHEQUE\003\OK.FLG goto M005
rfrompcb C:\BCM\eCheque\TRScript\003\file_upload.dtt
ren H:\RETAIL\eCHEQUE\003\OK.FLG OK.FLG%datesuffix%%timesuffix%
move H:\RETAIL\eCHEQUE\003\OK.FLG%datesuffix%%timesuffix% H:\RETAIL\eCHEQUE\003\bk
ren H:\RETAIL\eCHEQUE\003\chequef.csv chequef.csv%datesuffix%%timesuffix%
move H:\RETAIL\eCHEQUE\003\chequef.csv%datesuffix%%timesuffix% H:\RETAIL\eCHEQUE\003\bk

:M005
if NOT exist H:\RETAIL\eCHEQUE\005\OK.FLG goto M006
rfrompcb C:\BCM\eCheque\TRScript\005\file_upload.dtt
ren H:\RETAIL\eCHEQUE\005\OK.FLG OK.FLG%datesuffix%%timesuffix%
move H:\RETAIL\eCHEQUE\005\OK.FLG%datesuffix%%timesuffix% H:\RETAIL\eCHEQUE\005\bk
ren H:\RETAIL\eCHEQUE\005\chequef.csv chequef.csv%datesuffix%%timesuffix%
move H:\RETAIL\eCHEQUE\005\chequef.csv%datesuffix%%timesuffix% H:\RETAIL\eCHEQUE\005\bk

:M006
if NOT exist H:\RETAIL\eCHEQUE\006\OK.FLG goto M008
rfrompcb C:\BCM\eCheque\TRScript\006\file_upload.dtt
ren H:\RETAIL\eCHEQUE\006\OK.FLG OK.FLG%datesuffix%%timesuffix%
move H:\RETAIL\eCHEQUE\006\OK.FLG%datesuffix%%timesuffix% H:\RETAIL\eCHEQUE\006\bk
ren H:\RETAIL\eCHEQUE\006\chequef.csv chequef.csv%datesuffix%%timesuffix%
move H:\RETAIL\eCHEQUE\006\chequef.csv%datesuffix%%timesuffix% H:\RETAIL\eCHEQUE\006\bk

:M008
if NOT exist H:\RETAIL\eCHEQUE\008\OK.FLG goto M012
rfrompcb C:\BCM\eCheque\TRScript\008\file_upload.dtt
ren H:\RETAIL\eCHEQUE\008\OK.FLG OK.FLG%datesuffix%%timesuffix%
move H:\RETAIL\eCHEQUE\008\OK.FLG%datesuffix%%timesuffix% H:\RETAIL\eCHEQUE\008\bk
ren H:\RETAIL\eCHEQUE\008\chequef.csv chequef.csv%datesuffix%%timesuffix%
move H:\RETAIL\eCHEQUE\008\chequef.csv%datesuffix%%timesuffix% H:\RETAIL\eCHEQUE\008\bk

:M012
if NOT exist H:\RETAIL\eCHEQUE\012\OK.FLG goto M016
rfrompcb C:\BCM\eCheque\TRScript\012\file_upload.dtt
ren H:\RETAIL\eCHEQUE\012\OK.FLG OK.FLG%datesuffix%%timesuffix%
move H:\RETAIL\eCHEQUE\012\OK.FLG%datesuffix%%timesuffix% H:\RETAIL\eCHEQUE\012\bk
ren H:\RETAIL\eCHEQUE\012\chequef.csv chequef.csv%datesuffix%%timesuffix%
move H:\RETAIL\eCHEQUE\012\chequef.csv%datesuffix%%timesuffix% H:\RETAIL\eCHEQUE\012\bk

:M016
if NOT exist H:\RETAIL\eCHEQUE\016\OK.FLG goto M022
rfrompcb C:\BCM\eCheque\TRScript\016\file_upload.dtt
ren H:\RETAIL\eCHEQUE\016\OK.FLG OK.FLG%datesuffix%%timesuffix%
move H:\RETAIL\eCHEQUE\016\OK.FLG%datesuffix%%timesuffix% H:\RETAIL\eCHEQUE\016\bk
ren H:\RETAIL\eCHEQUE\016\chequef.csv chequef.csv%datesuffix%%timesuffix%
move H:\RETAIL\eCHEQUE\016\chequef.csv%datesuffix%%timesuffix% H:\RETAIL\eCHEQUE\016\bk

:M022
if NOT exist H:\RETAIL\eCHEQUE\022\OK.FLG goto M023
rfrompcb C:\BCM\eCheque\TRScript\022\file_upload.dtt
ren H:\RETAIL\eCHEQUE\022\OK.FLG OK.FLG%datesuffix%%timesuffix%
move H:\RETAIL\eCHEQUE\022\OK.FLG%datesuffix%%timesuffix% H:\RETAIL\eCHEQUE\022\bk
ren H:\RETAIL\eCHEQUE\022\chequef.csv chequef.csv%datesuffix%%timesuffix%
move H:\RETAIL\eCHEQUE\022\chequef.csv%datesuffix%%timesuffix% H:\RETAIL\eCHEQUE\022\bk

:M023
if NOT exist H:\RETAIL\eCHEQUE\023\OK.FLG goto M025
rfrompcb C:\BCM\eCheque\TRScript\023\file_upload.dtt
ren H:\RETAIL\eCHEQUE\023\OK.FLG OK.FLG%datesuffix%%timesuffix%
move H:\RETAIL\eCHEQUE\023\OK.FLG%datesuffix%%timesuffix% H:\RETAIL\eCHEQUE\023\bk
ren H:\RETAIL\eCHEQUE\023\chequef.csv chequef.csv%datesuffix%%timesuffix%
move H:\RETAIL\eCHEQUE\023\chequef.csv%datesuffix%%timesuffix% H:\RETAIL\eCHEQUE\023\bk

:M025
if NOT exist H:\RETAIL\eCHEQUE\025\OK.FLG goto M026
rfrompcb C:\BCM\eCheque\TRScript\025\file_upload.dtt
ren H:\RETAIL\eCHEQUE\025\OK.FLG OK.FLG%datesuffix%%timesuffix%
move H:\RETAIL\eCHEQUE\025\OK.FLG%datesuffix%%timesuffix% H:\RETAIL\eCHEQUE\025\bk
ren H:\RETAIL\eCHEQUE\025\chequef.csv chequef.csv%datesuffix%%timesuffix%
move H:\RETAIL\eCHEQUE\025\chequef.csv%datesuffix%%timesuffix% H:\RETAIL\eCHEQUE\025\bk

:M026
if NOT exist H:\RETAIL\eCHEQUE\026\OK.FLG goto M028
rfrompcb C:\BCM\eCheque\TRScript\026\file_upload.dtt
ren H:\RETAIL\eCHEQUE\026\OK.FLG OK.FLG%datesuffix%%timesuffix%
move H:\RETAIL\eCHEQUE\026\OK.FLG%datesuffix%%timesuffix% H:\RETAIL\eCHEQUE\026\bk
ren H:\RETAIL\eCHEQUE\026\chequef.csv chequef.csv%datesuffix%%timesuffix%
move H:\RETAIL\eCHEQUE\026\chequef.csv%datesuffix%%timesuffix% H:\RETAIL\eCHEQUE\026\bk

:M028
if NOT exist H:\RETAIL\eCHEQUE\028\OK.FLG goto M032
rfrompcb C:\BCM\eCheque\TRScript\028\file_upload.dtt
ren H:\RETAIL\eCHEQUE\028\OK.FLG OK.FLG%datesuffix%%timesuffix%
move H:\RETAIL\eCHEQUE\028\OK.FLG%datesuffix%%timesuffix% H:\RETAIL\eCHEQUE\028\bk
ren H:\RETAIL\eCHEQUE\028\chequef.csv chequef.csv%datesuffix%%timesuffix%
move H:\RETAIL\eCHEQUE\028\chequef.csv%datesuffix%%timesuffix% H:\RETAIL\eCHEQUE\028\bk

:M032
if NOT exist H:\RETAIL\eCHEQUE\032\OK.FLG goto M033
rfrompcb C:\BCM\eCheque\TRScript\032\file_upload.dtt
ren H:\RETAIL\eCHEQUE\032\OK.FLG OK.FLG%datesuffix%%timesuffix%
move H:\RETAIL\eCHEQUE\032\OK.FLG%datesuffix%%timesuffix% H:\RETAIL\eCHEQUE\032\bk
ren H:\RETAIL\eCHEQUE\032\chequef.csv chequef.csv%datesuffix%%timesuffix%
move H:\RETAIL\eCHEQUE\032\chequef.csv%datesuffix%%timesuffix% H:\RETAIL\eCHEQUE\032\bk

:M033
if NOT exist H:\RETAIL\eCHEQUE\033\OK.FLG goto EXIT
rfrompcb C:\BCM\eCheque\TRScript\033\file_upload.dtt
ren H:\RETAIL\eCHEQUE\033\OK.FLG OK.FLG%datesuffix%%timesuffix%
move H:\RETAIL\eCHEQUE\033\OK.FLG%datesuffix%%timesuffix% H:\RETAIL\eCHEQUE\033\bk
ren H:\RETAIL\eCHEQUE\033\chequef.csv chequef.csv%datesuffix%%timesuffix%
move H:\RETAIL\eCHEQUE\033\chequef.csv%datesuffix%%timesuffix% H:\RETAIL\eCHEQUE\033\bk

:EXIT
PAUSE
