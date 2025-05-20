#!/bin/bash
PATH=/bin:/sbin:/usr/bin:/usr/sbin:/opt/bin:/opt/sbin:~/bin
export PATH

# Check if user is root
if [ $(id -u) != "0" ]; then
    echo "Error: You must be root to run this script, please use root to install"
    exit 1
fi

clear
echo "========================================================================="
echo "A tool to auto-compile & install MySQL 5.6.22 on Redhat/CentOS Linux "
echo "========================================================================="
cur_dir=$(pwd)

#set mysql root password
	echo "==========================="

	mysqlrootpwd="root"
	echo -e "Please input the root password of mysql:"
	read -p "(Default password: root):" mysqlrootpwd
	if [ "$mysqlrootpwd" = "" ]; then
		mysqlrootpwd="root"
	fi
	echo "==========================="
	echo "MySQL root password:$mysqlrootpwd"
	echo "==========================="

#which MySQL Version do you want to install?
echo "==========================="

	isinstallmysql56="n"
	echo "Install MySQL 5.6.22,Please input y"
	read -p "(Please input y , n):" isinstallmysql56

	case "$isinstallmysql56" in
	y|Y|Yes|YES|yes|yES|yEs|YeS|yeS)
	echo "You will install MySQL 5.6.22"
	isinstallmysql56="y"
	;;
	*)
	echo "INPUT error,You will exit install MySQL 5.6.22"
	isinstallmysql56="n"
    exit
	esac

	get_char()
	{
	SAVEDSTTY=`stty -g`
	stty -echo
	stty cbreak
	dd if=/dev/tty bs=1 count=1 2> /dev/null
	stty -raw
	stty echo
	stty $SAVEDSTTY
	}
	echo ""
	echo "Press any key to start...or Press Ctrl+c to cancel"
	char=`get_char`

# Initialize  the installation related content.
function InitInstall()
{
	cat /etc/issue
	uname -a
	MemTotal=`free -m | grep Mem | awk '{print  $2}'`  
	echo -e "\n Memory is: ${MemTotal} MB "
	#Set timezone
	rm -rf /etc/localtime
	ln -s /usr/share/zoneinfo/Asia/Shanghai /etc/localtime

    #Synchronization time
	yum install -y ntp
	ntpdate -u pool.ntp.org
	date

    #Delete Old Mysql program
	rpm -qa|grep mysql
	rpm -e mysql
	yum -y remove mysql-server mysql mysql-libs
	yum -y remove php-mysql

	yum -y install yum-fastestmirror
	#yum -y update

	#Disable SeLinux
	if [ -s /etc/selinux/config ]; then
	sed -i 's/SELINUX=enforcing/SELINUX=disabled/g' /etc/selinux/config
	fi
    setenforce 0

    #Install Compile MySQL related procedures
	for packages in make cmake ncurses-devel bison bison-devel libaio-devel gcc gcc-c++ openssl openssl-devel ncurses ncurses-devel net-tools perl-Data-Dumper perl;
	do yum -y install $packages; done

}

#Check the mysql installation file exists, there is no download automatically.
function CheckAndDownloadFiles()
{
echo "============================check mysql files=================================="
if [ "$isinstallmysql56" = "y" ]; then
	if [ -s mysql-5.6.22.tar.gz ]; then
	  echo "mysql-5.6.22.tar.gz [found]"
	  else
	  echo "Error: mysql-5.6.22.tar.gz not found!!!download now......"
	  wget -c http://dev.mysql.com/get/Downloads/MySQL-5.6/mysql-5.6.22.tar.gz
	fi
echo "============================check files=================================="
fi
}

#Installation of depend on and optimization options.
function InstallDependsAndOpt()
{
cd $cur_dir

cat >>/etc/security/limits.conf<<eof
* soft nproc 65535
* hard nproc 65535
* soft nofile 65535
* hard nofile 65535
eof

echo "fs.file-max=65535" >> /etc/sysctl.conf
}

#Install MySQL
function InstallMySQL56()
{
echo "============================Install MySQL 5.6.22=================================="
cd $cur_dir

#Backup old my.cnf
#rm -f /etc/my.cnf
if [ -s /etc/my.cnf ]; then
    mv /etc/my.cnf /etc/my.cnf.`date +%Y%m%d%H%M%S`.bak
fi

#mysql directory configuration
mkdir -p /opt/mysql-5.6.22
ln -s /opt/mysql-5.6.22 /opt/mysql
if [ -d /data ] && [ -d /log ];then
    ln -s /data /opt/mysql/data
    ln -s /log /opt/mysql/log
    mkdir -p /opt/mysql/run
else
    mkdir -p /opt/mysql/{data,log,run}
fi

groupadd mysql
useradd -s /sbin/nologin -M -g mysql mysql
if [ -d /data ] && [ -d /log ];then
    chown -R mysql.mysql /opt/mysql
    chown -R mysql.mysql /data
    chown -R mysql.mysql /log
fi

tar zxf mysql-5.6.22.tar.gz
cd mysql-5.6.22/
mkdir source_downloads
if [ -s gmock-1.6.0.zip ];then
    cp ../gmock-1.6.0.zip source_downloads
fi

# CFLAGS="-O3 -g -fno-exceptions -static-libgcc -fno-omit-frame-pointer -fno-strict-aliasing"
# CXX=g++
# CXXFLAGS="-O3 -g -fno-exceptions -fno-rtti -static-libgcc -fno-omit-frame-pointer -fno-strict-aliasing"
# export CFLAGS CXX CXXFLAGS

cmake \
-DCMAKE_INSTALL_PREFIX=/opt/mysql \
-DMYSQL_DATADIR=/opt/mysql/data \
-DSYSCONFDIR=/etc \
-DWITH_MYISAM_STORAGE_ENGINE=1 \
-DWITH_INNOBASE_STORAGE_ENGINE=1 \
-DWITH_MEMORY_STORAGE_ENGINE=1 \
-DWITH_READLINE=1 \
-DWITH_PARTITION_STORAGE_ENGINE=1 \
-DENABLED_LOCAL_INFILE=1 \
-DEXTRA_CHARSETS=all \
-DDEFAULT_CHARSET=utf8 \
-DDEFAULT_COLLATION=utf8_general_ci 
-DWITH_SSL=system \
-DWITH_ZLIB=system \
-DMYSQL_UNIX_ADDR=/opt/mysql/run/mysql.sock \
-DMYSQL_TCP_PORT=3306 \
-DMYSQL_USER=mysql \
-DENABLE_DOWNLOADS=1 \
-DWITH_DEBUG=0

make -j `cat /proc/cpuinfo | grep processor| wc -l` && make install

if [ -s ../my_defaults.cnf ]; then
    echo "mysql user-defined config is [found]"
    cp ../my_defaults.cnf /etc/my.cnf
    else
    cp support-files/my-default.cnf /etc/my.cnf
fi

/opt/mysql/scripts/mysql_install_db --defaults-file=/etc/my.cnf --basedir=/opt/mysql --datadir=/opt/mysql/data --user=mysql --explicit_defaults_for_timestamp
cp support-files/mysql.server /etc/init.d/mysql
chmod 755 /etc/init.d/mysql
chkconfig --add mysql
chkconfig mysql on

cat > /etc/ld.so.conf.d/mysql.conf<<EOF
/opt/mysql/lib
EOF
ldconfig

ln -s /opt/mysql/lib/mysql /usr/lib/mysql
ln -s /opt/mysql/include/mysql /usr/include/mysql
if [ -d "/proc/vz" ];then
ulimit -s unlimited
fi
/etc/init.d/mysql start

# ln -s /opt/mysql/bin/mysql /usr/bin/mysql
# ln -s /opt/mysql/bin/mysqldump /usr/bin/mysqldump
# ln -s /opt/mysql/bin/myisamchk /usr/bin/myisamchk
# ln -s /opt/mysql/bin/mysqld_safe /usr/bin/mysqld_safe

cat > /etc/profile.d/mysql.sh <<EOF
PATH=/opt/mysql/bin:\$PATH
export PATH
EOF
source /etc/profile

/opt/mysql/bin/mysqladmin -u root password $mysqlrootpwd

cat > /tmp/mysql_sec_script<<EOF
use mysql;
update user set password=password('$mysqlrootpwd') where user='root';
delete from user where not (user='root') ;
delete from user where user='root' and password=''; 
drop database test;
DROP USER ''@'%';
flush privileges;
EOF

/opt/mysql/bin/mysql -u root -p$mysqlrootpwd -h localhost < /tmp/mysql_sec_script

rm -f /tmp/mysql_sec_script
source /etc/profile
/etc/init.d/mysql restart
#/etc/init.d/mysql stop
echo "============================MySQL 5.6.22 install completed========================="
}


function CheckInstall()
{
echo "===================================== Check install ==================================="
clear
ismysql=""
echo "Checking..."

if [ -s /opt/mysql/bin/mysql ] && [ -s /opt/mysql/bin/mysqld_safe ] && [ -s /etc/my.cnf ]; then
  echo "MySQL: OK"
  ismysql="ok"
  else
  echo "Error: /opt/mysql not found!!!MySQL install failed."
fi

if [ "$ismysql" = "ok" ]; then
echo "Install MySQL 5.6.22 completed! enjoy it."
echo "========================================================================="
netstat -ntl
else
echo "Sorry,Failed to install MySQL!"
echo "You can tail /root/mysql-install.log from your server."
fi
}

#The installation log
InitInstall 2>&1 | tee /root/mysql-install.log
CheckAndDownloadFiles 2>&1 | tee -a /root/mysql-install.log
InstallDependsAndOpt 2>&1 | tee -a /root/mysql-install.log
InstallMySQL56 2>&1 | tee -a /root/mysql-install.log
CheckInstall 2>&1 | tee -a /root/mysql-install.log
