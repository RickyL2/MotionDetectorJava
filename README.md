# MotionDetectorJava

Hello. To use application, just open the securityCam executable jar file.
If you and some others would like to recieve a text message occasionaly when the program detects movement, simply open the users text document and enter the name, phone number, and cell phone carrier of the user.
example:

Ricky (626)628-9283 Verizon
Bob (909)324-2343 T-Mobile
Samantha (324)234-2453 AT&T

Having parenthesis in the phone number is not required, as long the digits of the phone number are present, it will work fine.
Having the phone carrier is absolutly necessary and must be exact
List of phone carriers are below, just copy and paste the ones you need to ensure it is entered in correctly

List of phone carriers:

AllTel
AT&T
Boost Mobile
Cricket
Sprint
T-Mobile
US Cellular
Verizon
Virgin Mobile

Different settings can be adjusted in the program.

frequency: how often program checks for movement. enter in milliseconds

notify time: how often program should send you a text message and take a photo. If movement occurs durring the time program is waiting to be allowed to send messages, the movement will not be reported.

pixel offset: how many pixels should program skip + 1 when analyzing motion. Should always be at least 1

error range: how leniant motion sensor should be. if 0, will report even slightest movement. too high and it will be blind to anything that goes on.

quantity of portions: how many portions should program divide image when analyzing.
