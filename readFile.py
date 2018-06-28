#-------------------------------------------------------------------------------
# Name: 
# Purpose:
#
# Author:      arodriguez
#
# Created:     10/05/2018

# Copyright:   (c) arodriguez 2018
# Licence:     
#-------------------------------------------------------------------------------

path = 'C:\\Alberto\\EclipsePortable\\Data\\workspace\\Ahorcado\\'
fout = open(path + "Esp.txt" , 'r')
fin = open(path + "Espmayorde4.txt.", 'a')
linea = 0
for line in fout:   
    if (len(line)>4):       
        fin.write(line)
        linea = linea  + 1
fout.close
fin.close();
    
    
    