# Project-3 by Joshua Durana and Dominic Li

Instructions
- Run server program on DC01-08 servers
- Run app program on another DC server
- Type 8 messages

### Server 1 Output
Message: 1
Write Successful
Version Number: 2
Replicas Updated: 9
Distinguished Site: 0

Message: 2
Write Successful
Version Number: 3
Replicas Updated: 10
Distinguished Site: 0

New Partition: [0, 1, 2, 3]
Connected to Server 1
Connected to Server 2
Connected to Server 3

Message: 3
Write Successful
Version Number: 4
Replicas Updated: 11
Distinguished Site: 0

Message: 4
Write Successful
Version Number: 5
Replicas Updated: 12
Distinguished Site: 0

New Partition: [0]

Message: 5
Write Unsuccessful
Version Number: 5
Replicas Updated: 12
Distinguished Site: 0

Message: 6
Write Unsuccessful
Version Number: 5
Replicas Updated: 12
Distinguished Site: 0

New Partition: [0]

Message: 7
Write Unsuccessful
Version Number: 5
Replicas Updated: 12
Distinguished Site: 0

Message: 8
Write Unsuccessful
Version Number: 5
Replicas Updated: 12
Distinguished Site: 0
Closing Server

### Server 2-4 Output
Message: 1
Write Successful
Version Number: 2
Replicas Updated: 9
Distinguished Site: 0

Message: 2
Write Successful
Version Number: 3
Replicas Updated: 10
Distinguished Site: 0

New Partition: [0, 1, 2, 3]
Connected to Server 0
Connected to Server 2
Connected to Server 3

Message: 3
Write Successful
Version Number: 4
Replicas Updated: 11
Distinguished Site: 0

Message: 4
Write Successful
Version Number: 5
Replicas Updated: 12
Distinguished Site: 0

New Partition: [1, 2, 3]
Connected to Server 2
Connected to Server 3

Message: 5
Write Successful
Version Number: 6
Replicas Updated: 13
Distinguished Site: 1

Message: 6
Write Successful
Version Number: 7
Replicas Updated: 14
Distinguished Site: 1

New Partition: [1, 2, 3, 4, 5, 6]
Connected to Server 2
Connected to Server 3
Connected to Server 4
Connected to Server 5
Connected to Server 6

Message: 7
Write Successful
Version Number: 8
Replicas Updated: 15
Distinguished Site: 1

Message: 8
Write Successful
Version Number: 9
Replicas Updated: 16
Distinguished Site: 1
Closing Server

### Server 5-7 Output
Message: 1
Write Successful
Version Number: 2
Replicas Updated: 9
Distinguished Site: 0

Message: 2
Write Successful
Version Number: 3
Replicas Updated: 10
Distinguished Site: 0

New Partition: [4, 5, 6, 7]
Connected to Server 4
Connected to Server 5
Connected to Server 7

Message: 3
Write Unsuccessful
Version Number: 3
Replicas Updated: 10
Distinguished Site: 0

Message: 4
Write Unsuccessful
Version Number: 3
Replicas Updated: 10
Distinguished Site: 0

New Partition: [4, 5, 6]
Connected to Server 4
Connected to Server 5

Message: 5
Write Unsuccessful
Version Number: 3
Replicas Updated: 10
Distinguished Site: 0

Message: 6
Write Unsuccessful
Version Number: 3
Replicas Updated: 10
Distinguished Site: 0

New Partition: [1, 2, 3, 4, 5, 6]
Connected to Server 1
Connected to Server 2
Connected to Server 3
Connected to Server 4
Connected to Server 5

Message: 7
Write Successful
Version Number: 8
Replicas Updated: 15
Distinguished Site: 1

Message: 8
Write Successful
Version Number: 9
Replicas Updated: 16
Distinguished Site: 1
Closing Server

### Server 8 Output
Message: 1
Write Successful
Version Number: 2
Replicas Updated: 9
Distinguished Site: 0

Message: 2
Write Successful
Version Number: 3
Replicas Updated: 10
Distinguished Site: 0

New Partition: [4, 5, 6, 7]
Connected to Server 4
Connected to Server 5
Connected to Server 6

Message: 3
Write Unsuccessful
Version Number: 3
Replicas Updated: 10
Distinguished Site: 0

Message: 4
Write Unsuccessful
Version Number: 3
Replicas Updated: 10
Distinguished Site: 0

New Partition: [7]

Message: 5
Write Unsuccessful
Version Number: 3
Replicas Updated: 10
Distinguished Site: 0

Message: 6
Write Unsuccessful
Version Number: 3
Replicas Updated: 10
Distinguished Site: 0

New Partition: [7]

Message: 7
Write Unsuccessful
Version Number: 3
Replicas Updated: 10
Distinguished Site: 0

Message: 8
Write Unsuccessful
Version Number: 3
Replicas Updated: 10
Distinguished Site: 0
Closing Server