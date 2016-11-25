<?php
/*
|||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||
|| Apple Push Notification Configurations
|||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||
*/


/*
|--------------------------------------------------------------------------
| APN Permission file 
|--------------------------------------------------------------------------
|
| Contains the certificate and private key, will end with .pem
| Full server path to this file is required.
|
*/
$config['PermissionFile'] = APPPATH.'../mimi_apns.pem';


/*
|--------------------------------------------------------------------------
| APN Private Key's Passphrase
|--------------------------------------------------------------------------
*/
$config['PassPhrase'] = 'mimi_apns';

/*
|--------------------------------------------------------------------------
| APN Services
|--------------------------------------------------------------------------
*/
/* dev: true
   release: false*/
$config['Sandbox'] = false;
$config['PushGatewaySandbox'] = 'ssl://gateway.sandbox.push.apple.com:2195';
$config['PushGateway'] = 'ssl://gateway.push.apple.com:2195';

$config['FeedbackGatewaySandbox'] = 'ssl://feedback.sandbox.push.apple.com:2196';
$config['FeedbackGateway'] = 'ssl://feedback.push.apple.com:2196';


/*
|--------------------------------------------------------------------------
| APN Connection Timeout
|--------------------------------------------------------------------------
*/
$config['Timeout'] = 60;


/*
|--------------------------------------------------------------------------
| APN Notification Expiry (seconds)
|--------------------------------------------------------------------------
| default: 86400 - one day
*/
$config['Expiry'] = 86400;