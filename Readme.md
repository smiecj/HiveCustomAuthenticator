# Custom hive authenticator
## build jar

## hiveserver2's configuration
### hive-site.xml
```
    <property>
      <name>hive.server2.authentication</name>
      <value>CUSTOM</value>
    </property>

    <property>
      <name>hive.server2.custom.authentication.class</name>
      <value>com.smiecj.hive.CustomSimpleAuthenticator</value>
    </property>

    <property>
      <name>hive.server2.enable.doAs</name>
      <value>true</value>
    </property>
```

## hue's configuration
### hue.ini
```
[beeswax]
  auth_username=hive
  auth_password=hive_123
```

### apps/beeswax/src/beeswax/server/hive_server2_lib.py
```
  def get_security(self):
    ......

    if self.query_server['server_name'].startswith('impala'):
      ......
    else:
      ......
      use_sasl = hive_mechanism in ('KERBEROS', 'NONE', 'LDAP', 'PAM', 'CUSTOM')
      ......
```

## refer
[blog-自定义HiveServer2的用户安全认证](http://lxw1234.com/archives/2016/01/600.htm)
