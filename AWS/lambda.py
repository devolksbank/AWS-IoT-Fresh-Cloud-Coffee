import boto3
import logging
from time import time
from boto3.dynamodb.conditions import Key

statusTableName = "<insert-device-status-table-here>"
deviceIdTableName = "deviceIds"
snsTopic = "<insert-topic-arn-here>"

logger = logging.getLogger()
ddb = boto3.resource('dynamodb')


def sendPushNotification(message):
    sns = boto3.client('sns')
    return sns.publish(TopicArn=snsTopic,Message=message, MessageStructure='string')
    
def getDeviceStatus(deviceId):
    statusTable = ddb.Table(statusTableName)
    result = statusTable.query(KeyConditionExpression=Key('deviceID').eq(deviceId), ScanIndexForward=False)["Items"]
    return(result[0]["status"] if result else "Working")

def getDeviceId(serialNumber):
    table = ddb.Table('deviceIds')
    result = table.get_item(Key = {'iot-serialNumber':serialNumber})
    if 'Item' not in result:
        logger.error("Device " + serialNumber + " could not be found.")
        return
    
    return(result['Item']['deviceId'])

def markAsDefect(deviceId):
    table = ddb.Table(statusTableName)
    item = {"deviceID":deviceId, 
        "timestamp":round(time()*1000),
        "status":"Defect"}
    return(table.put_item(Item=item))
    

def lambda_handler(event, context):
    serialNumber = event['serialNumber']
    deviceId = getDeviceId(serialNumber)
    
    deviceStatus = getDeviceStatus(deviceId)
    markAsDefect(deviceId)
    
    if("Defect" not in deviceStatus):
        sendPushNotification("Device " + str(deviceId) + " has been marked as defect.")