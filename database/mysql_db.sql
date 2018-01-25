CREATE USER 'longlh'@'localhost' IDENTIFIED BY 'SolonG';
CREATE USER 'longlh'@'127.0.0.1' IDENDIFIED BY 'SolonG';
CREATE USER 'longlh'@'%' IDENTIFIED BY 'SolonG';


grant all on solar.* to 'longlh'@'%'

sudo service mysql stop
sudo service mysql start

