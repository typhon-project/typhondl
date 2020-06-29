#!/bin/bash
echo "sleeping for 10 seconds"
sleep 10

echo init_set.sh time now: `date +"%T" `
mongo --host vehicledatadb:27017 <<EOF
  var cfg = {
    "_id": "vehicledatadbReplset",
    "version": 1,
    "members": [
      {
        "_id": 0,
        "host": "vehicledatadb:27017"
      }
      ,{
        "_id": 1,
        "host": "vehicledatadb-replica1:27017"
      }
      ,{
        "_id": 2,
        "host": "vehicledatadb-replica2:27017"
      }
      ,{
        "_id": 3,
        "host": "vehicledatadb-replica3:27017"
      }
    ]
  };
  rs.initiate(cfg);
EOF
