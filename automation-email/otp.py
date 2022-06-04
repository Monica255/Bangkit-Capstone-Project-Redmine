from flask import Flask, render_template, request, jsonify
import firebase_admin
from firebase_admin import credentials
from firebase_admin import db
import smtplib
import ssl
import math
import random
from email.message import EmailMessage

app = Flask(__name__)

cred = credentials.Certificate("C:\\Users\\ASUS\\PycharmProjects\\bangkit_capstone\\redmineKey.json")

firebase = firebase_admin.initialize_app(cred,
                                         {'databaseURL': 'https://redmine-350506-default-rtdb.asia-southeast1.firebasedatabase.app/'}
                                         )

root = db.reference()


@app.route('/sendOtp', methods=['POST'])
def sendEmail():
    user_email = request.form.get("email")
    for string in user_email:
        if "." == string:
            db_email = user_email.replace(string, "")

    digits = "0123456789"
    otp = ""

    for i in range(6):
        random_number = math.floor(random.random() * 10)
        otp += str(random_number)

    subject = "REDMINE"
    call_otp = otp
    body = "Hello Redminers, \n\nAvoid scams! Do not give the OTP code to anyone. Redmine OTP code: {}.".format(
        call_otp)
    email_sender = "nararyanirankara@gmail.com"
    email_receiver = user_email
    password = "ognbgktfhkfcunra"

    message = EmailMessage()
    message["From"] = email_sender
    message["To"] = email_receiver
    message["Subject"] = subject

    html = f"""
    <html>
      <body>
        <h1>{subject}</h1>
        <p>{body}</p>
      </body>
    </html>
    """
    message.add_alternative(html, subtype="html")

    context = ssl.create_default_context()

    with smtplib.SMTP_SSL("smtp.gmail.com", 465, context=context) as server:
        server.login(email_sender, password)
        server.sendmail(email_sender, email_receiver, message.as_string())

        data = {"otpCode": call_otp}
        db.reference("otp_codes").child(db_email).set(data)

    email_response = {
        "otpCode": call_otp,
        "email": user_email,
        "databaseEmail": db_email

    }

    return jsonify(email_response)

#
if __name__ == "__main__":
    # app.run(host="localhost", debug=True)
    app.run(debug=True, host="0.0.0.0")