
:M001
if NOT exist H:\RETAIL\eCHEQUE\001\OK.FLG goto M003
rfrompcb H:\RETAIL\eCHEQUE\001\file_upload.dtt
del H:\RETAIL\eCHEQUE\001\OK.FLG
del H:\RETAIL\eCHEQUE\001\chequef.csv
:M003
if NOT exist H:\RETAIL\eCHEQUE\003\OK.FLG goto M005
rfrompcb H:\RETAIL\eCHEQUE\003\file_upload.dtt
del H:\RETAIL\eCHEQUE\003\OK.FLG
del H:\RETAIL\eCHEQUE\003\chequef.csv
:M005
if NOT exist H:\RETAIL\eCHEQUE\005\OK.FLG goto M006
rfrompcb H:\RETAIL\eCHEQUE\005\file_upload.dtt
del H:\RETAIL\eCHEQUE\005\OK.FLG
del H:\RETAIL\eCHEQUE\005\chequef.csv
:M006
if NOT exist H:\RETAIL\eCHEQUE\006\OK.FLG goto M008
rfrompcb H:\RETAIL\eCHEQUE\006\file_upload.dtt
del H:\RETAIL\eCHEQUE\006\OK.FLG
del H:\RETAIL\eCHEQUE\006\chequef.csv
:M008
if NOT exist H:\RETAIL\eCHEQUE\008\OK.FLG goto M012
rfrompcb H:\RETAIL\eCHEQUE\008\file_upload.dtt
del H:\RETAIL\eCHEQUE\008\OK.FLG
del H:\RETAIL\eCHEQUE\008\chequef.csv
:M012
if NOT exist H:\RETAIL\eCHEQUE\012\OK.FLG goto M016
rfrompcb H:\RETAIL\eCHEQUE\012\file_upload.dtt
del H:\RETAIL\eCHEQUE\012\OK.FLG
del H:\RETAIL\eCHEQUE\012\chequef.csv
:M016
if NOT exist H:\RETAIL\eCHEQUE\016\OK.FLG goto M022
rfrompcb H:\RETAIL\eCHEQUE\016\file_upload.dtt
del H:\RETAIL\eCHEQUE\016\OK.FLG
del H:\RETAIL\eCHEQUE\016\chequef.csv
:M022
if NOT exist H:\RETAIL\eCHEQUE\022\OK.FLG goto M023
rfrompcb H:\RETAIL\eCHEQUE\022\file_upload.dtt
del H:\RETAIL\eCHEQUE\022\OK.FLG
del H:\RETAIL\eCHEQUE\022\chequef.csv
:M023
if NOT exist H:\RETAIL\eCHEQUE\023\OK.FLG goto M025
rfrompcb H:\RETAIL\eCHEQUE\023\file_upload.dtt
del H:\RETAIL\eCHEQUE\023\OK.FLG
del H:\RETAIL\eCHEQUE\023\chequef.csv
:M025
if NOT exist H:\RETAIL\eCHEQUE\025\OK.FLG goto M026
rfrompcb H:\RETAIL\eCHEQUE\025\file_upload.dtt
del H:\RETAIL\eCHEQUE\025\OK.FLG
del H:\RETAIL\eCHEQUE\025\chequef.csv
:M026
if NOT exist H:\RETAIL\eCHEQUE\026\OK.FLG goto M028
rfrompcb H:\RETAIL\eCHEQUE\026\file_upload.dtt
del H:\RETAIL\eCHEQUE\026\OK.FLG
del H:\RETAIL\eCHEQUE\026\chequef.csv
:M028
if NOT exist H:\RETAIL\eCHEQUE\028\OK.FLG goto M032
rfrompcb H:\RETAIL\eCHEQUE\028\file_upload.dtt
del H:\RETAIL\eCHEQUE\028\OK.FLG
del H:\RETAIL\eCHEQUE\028\chequef.csv
:M032
if NOT exist H:\RETAIL\eCHEQUE\032\OK.FLG goto M033
rfrompcb H:\RETAIL\eCHEQUE\032\file_upload.dtt
del H:\RETAIL\eCHEQUE\032\OK.FLG
del H:\RETAIL\eCHEQUE\032\chequef.csv
:M033
if NOT exist H:\RETAIL\eCHEQUE\033\OK.FLG goto EXIT
rfrompcb H:\RETAIL\eCHEQUE\033\file_upload.dtt
del H:\RETAIL\eCHEQUE\033\OK.FLG
del H:\RETAIL\eCHEQUE\033\chequef.csv
:EXIT
PAUSE