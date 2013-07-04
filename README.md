vcloud-client - a tool to manage vCloud datacenter objects
-----------------------------------------------------------

**vcloud-client** is a tool to manage objects in vCloud Director, using the vCloud API.

There are several modes of operation, all of which can be found in the synopsis below. Each mode has a different set of required and optional arguments, which can also be found in the synopsis. Help mode can be used in a context sensitive manner. For example, *-h*will show all the modes, and *-h ADDVM*will show help about the ADDVM mode.

All commands require proper authentication. This can be provided on the command line by using *-u -p -s* or by creating a configuration file and specifying it with *-c config-file*
**vcloud-client** is licensed under the GPLv3 license. For more information, see the *LICENSE* file.
This project uses libraries and routines which may have a different license. Refer to the included licenses in the source files and/or JAR files for more information.

BUILDING
--------
Building **vcloud-client** requires the following:

1. Oracle Java or OpenJDK >= 6
2. Apache Ant >= 1.8

Then you can simply call `ant dist` to create a *dist* folder with everything vcloud-client needs to run. You can also use `ant package-tar` to create a tarball

SYNOPSIS
--------
	
	vcloud-client -a | -h <COMMAND> | -l <TYPE> | -r | -t | -v | -w | -x | -y | -z [-c <FILE>]   [-p <PASSWORD>]  [-s <SERVER>]  [-u <USER>]

**HELP**

	vcloud-client -h <COMMAND>

**VERSION**

	vcloud-client -v

**LIST**

	vcloud-client [-c <FILE>] -l <TYPE> [-p <PASSWORD>] [-s <SERVER>] [-u <USER>]

**ADDVM**

	vcloud-client -a [-c <FILE>] --catalog <CATALOG> --description <DESC> --fqdn <FQDN> --ip <IP> --network <NETWORK> --organization <ORG> [-p <PASSWORD>] [-s <SERVER>] --template <TEMPLATE> [-u <USER>] --vapp <VAPP> --vdc <VDC>

**REMOVEVM**

	vcloud-client [-c <FILE>] --organization <ORG> [-p <PASSWORD>] -r [-s <SERVER>] [-u <USER>] --vapp <VAPP> --vdc <VDC> --vm <VM>

**POWERONVM**

	vcloud-client [-c <FILE>] --organization <ORG> [-p <PASSWORD>] [-s <SERVER>] [-u <USER>] --vapp <VAPP> --vdc <VDC> --vm <VM> -y

**POWEROFFVM**

	vcloud-client [-c <FILE>] --organization <ORG> [-p <PASSWORD>] [-s <SERVER>] -t [-u <USER>] --vapp <VAPP> --vdc <VDC> --vm <VM>

**SHUTDOWNVM**

	vcloud-client [-c <FILE>] --organization <ORG> [-p <PASSWORD>] [-s <SERVER>] [-u <USER>] --vapp <VAPP> --vdc <VDC> --vm <VM> -z

**RESIZEDISK**

	vcloud-client [-c <FILE>] --disk-name <DISK> --disk-size <SIZE> --organization <ORG> [-p <PASSWORD>] [-s <SERVER>] [-u <USER>] --vapp <VAPP> --vdc <VDC> --vm <VM> -w

**CONSOLIDATEVM**

	vcloud-client [-c <FILE>] --organization <ORG> [-p <PASSWORD>] [-s <SERVER>] [-u <USER>] --vapp <VAPP> --vdc <VDC> --vm <VM> -x

OPTIONS
-------
**-a** **--add-vm** *arg* 

Add a new VM from a vApp Template to an existing vApp

**--catalog** *CATALOG* 

Select this catalog

**-c** **--config** *FILE* 

Use a configuration file

**--description** *DESC* 

Description of object to create

**--disk-name** *DISK* 

Name of disk to resize

**--disk-size** *SIZE* 

New size of disk in MB

**--fqdn** *FQDN* 

Name of object to create

**-h** **--help** *COMMAND* 

Show help and examples

**--ip** *IP* 

IP of the object to create

**-l** **--list** *TYPE* 

List vCloud objects (org|vdc|vapp|catalog|vm)

**--network** *NETWORK* 

Network of the object to create

**--organization** *ORG* 

Select this Organization

**-p** **--password** *PASSWORD* 

vCloud Director password

**-r** **--remove-vm** *arg* 

Remove a VM from an existing vApp

**-s** **--server** *SERVER* 

vCloud Director server URI

**--template** *TEMPLATE* 

Select this template

**-t** **--poweroff-vm** *arg* 

Stop an existing VM (hard shutdown)

**-u** **--username** *USER* 

vCloud Director username

**--vapp** *VAPP* 

Select this vApp

**--vdc** *VDC* 

Select this Virtual Data Center

**--vm** *VM* 

Select this VM

**-v** **--version** *arg* 

Show version information

**-w** **--resize-disk** *arg* 

Resize the disk of an existing VM

**-x** **--consolidate-vm** *arg* 

Consolidate all disks of an existing VM

**-y** **--poweron-vm** *arg* 

Start an existing VM

**-z** **--shutdown-vm** *arg* 

Shutdown an existing VM (soft shutdown)

CONFIGURATION
-------------
All command line parameters can optionally be provided using a configuration file. Exception on this are the mode selectors. The configuration file uses a simple format, which is:

	option=value

*option* is the same as the long options which can be specified on the command line. For example, this is a valid configuration line:

	username=user@Organization

Configuration options are parsed in the following order: 

1. The *-c* option.
2. All options provided on the command line, in the order they are specified.
It is possible to override already specified configuration options by specifying them again. Duplicate options will take the value of the last one specified. An example configuration file can be found in the distribution package.

BUGS
----
No major known bugs exist at this time.

AUTHOR
------
Nick Douma (n.douma@nekoconeko.nl)

