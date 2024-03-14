#!bin/bash

flag=0

oc project <nome del progetto>

oc get pod > temp.txt

while read p
do
    oc get ${pod} --show-labels | tr -s " " | cut -d " " -f 6 | tr "," "\n" > temp2.txt
    while read l
    do 
        label=$(echo l | cut -d "=" -f 1)
        #controllo se ci sono tutte le label per ogni pod
        if [ label = ]; then
            ((flag=1))
        elif [ label =  ]; then 
            ((flag=1))
        elif [ label =  ]; then  
            ((flag=1))
        elif [ label =  ]; then 
            ((flag=1))
        elif [ label =  ]; then 
            ((flag=1))
        elif [ label =  ]; then 
            ((flag=1))
        fi

        #se non c'è una label cosa faccio?
        if [ flag -eq 0 ]; then

        fi
        
        ((flag=0))

    done < temp2.txt

    rm temp2.txt

done < temp.txt

rm temp.txt