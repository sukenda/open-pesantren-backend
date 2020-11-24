// docker-compose -f mongodb-compose.yaml up -d
// docker exec -it mongodb /bin/bash
// mongo -u root -p Pas@123 --authenticationDatabase admin
// mongo -u open-pesantren-user -p open-pesantren-pass --authenticationDatabase open-pesantren
// use open-pesantren
db.createUser({
    user: 'open-pesantren-user',
    pwd: 'open-pesantren-pass',
    roles: [
        {
            role: 'readWrite',
            db: 'open-pesantren'
        }
    ]
})


