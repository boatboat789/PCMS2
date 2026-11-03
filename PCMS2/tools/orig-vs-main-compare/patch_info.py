import re
props={}
for l in open('<WORKTREE>/PCMS2/src/test/resources/database.properties',encoding='utf-8'):
    l=l.rstrip('\r\n')
    if '=' in l and not l.lstrip().startswith('#'):
        k,v=l.split('=',1); props[k.strip()]=v
files={'pcms':'SqlPCMSInfo.java','ppmm':'SqlPPMMInfo.java','sor':'SqlSORInfo.java','erp':'SqlAtechERPInfo.java'}
for p,f in files.items():
    s=open(f,encoding='utf-8',errors='replace').read()
    for field,key in (('_url','url'),('_username','username'),('_password','password')):
        pat=re.compile(r'^(?!\s*//)(\s*private static final String '+field+r'\s*=\s*)"[^"]*"(\s*;)',re.M)
        val=props['%s.db.%s'%(p,key)]
        if key!='url': assert chr(92) not in val and chr(34) not in val, (p,key)
        s,n=pat.subn(lambda m: m.group(1)+'"'+val+'"'+m.group(2),s,count=1)
        assert n==1,(f,field)
    open(f,'w',encoding='utf-8').write(s)
    u=re.search(r'^(?!\s*//)\s*private static final String _url\s*=\s*"([^"]*)"',s,re.M).group(1)
    print(f,'->',u[:75])
