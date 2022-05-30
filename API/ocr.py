import os
import tensorflow as tf
from flask import Flask, request, jsonify
import cv2
import numpy as np
import matplotlib.pyplot as plt
# import pyrebase
import firebase_admin
from firebase_admin import credentials
from firebase_admin import db

model = tf.keras.models.load_model("C:\\Users\\ASUS\\PycharmProjects\\bangkit_capstone\\ocr-model-Felix-5.h5")
# model.summary()

cred = credentials.Certificate("C:\\Users\\ASUS\\PycharmProjects\\bangkit_capstone\\redmineKey.json")
firebase = firebase_admin.initialize_app(cred,
                                         {'databaseURL': 'https://redmine-350506-default-rtdb.asia-southeast1.firebasedatabase.app/'}
                                         )
root = db.reference()

def get_word(image):
    try:
        img = cv2.imread(image)
        img = cv2.resize(img, (128, 128))
        img = cv2.cvtColor(img, cv2.COLOR_RGB2GRAY)

        # plt.imshow(img)
        # plt.show()
        img = np.array(img, dtype='float32')
        img = np.expand_dims(img, axis=2)
        img = img.reshape(-1, 128, 128, 1)
        prediction = model.predict(img)
        np.argmax(prediction)

        labelNames = "0123456789"
        labelNames += "ABCDEFGHIJKLMNOPQRSTUVWXYZ"
        return labelNames[np.argmax(prediction)]
    except:
        return ""

def bounding_box(image):
  gray = cv2.cvtColor(image, cv2.COLOR_RGB2GRAY)
  thresh = cv2.threshold(gray, 0, 255, cv2.THRESH_BINARY_INV + cv2.THRESH_OTSU)[1]
  kernal = cv2.getStructuringElement(cv2.MORPH_RECT, (3,13))
  dilate = cv2.dilate(thresh, kernal, iterations = 1)

  # Find contours, obtain bounding box, extract and save ROI
  ROI_number = 0
  contours = cv2.findContours(dilate, cv2.RETR_EXTERNAL, cv2.CHAIN_APPROX_SIMPLE)
  contours = contours[0] if len(contours) == 2 else contours[1]
  contours = sorted(contours, key = lambda x: cv2.boundingRect(x)[0])
  prediction = ""
  for c in contours:
      x,y,w,h = cv2.boundingRect(c)
      cv2.rectangle(image, (x, y), (x + w, y + h), (36,255,12), 2)
      ROI = image[y:y+h, x:x+w]
      cv2.imwrite('C:\\Users\\ASUS\\PycharmProjects\\bangkit_capstone\\ROI\\ROI_{}.png'.format(ROI_number), ROI)
      word = get_word('C:\\Users\\ASUS\\PycharmProjects\\bangkit_capstone\\ROI\\ROI_{}.png'.format(ROI_number))
      prediction += word
      ROI_number += 1

  return prediction

def get_name(image):
  name = image[680:800 ,950:2500]
  prediction = bounding_box(name)
  return prediction

def get_jenis_kelamin(image):
  jenis_kelamin = image[950:1050, 950:1800]
  prediction = bounding_box(jenis_kelamin)
  return prediction

app = Flask(__name__)

@app.route('/getOcr', methods=['POST'])
def predict():
    imagefile = request.files['imagefile']
    uid = request.form.get('uid')

    image_path = "C:\\Users\\ASUS\\PycharmProjects\\bangkit_capstone\\images\\"+imagefile.filename
    # image = imagefile.filename
    imagefile.save(image_path)

    image = cv2.imread(image_path)
    image = cv2.cvtColor(image, cv2.COLOR_BGR2RGB)
    nama = get_name(image)
    jenis_kelamin = get_jenis_kelamin(image)
    verified = bool(True)

    response_json = {
        "name": nama,
        "gender": jenis_kelamin,
        "verified": verified
    }

    new_users = {
        "name": nama,
        "verified": verified
    }

    new_users_data = {
        "gender": jenis_kelamin,
        "verified": verified
    }

    db.reference("users").child(uid).update(new_users)
    db.reference("users_data").child(uid).update(new_users_data)

    return jsonify(response_json)

if __name__ == "__main__":
    # app.run(host="localhost", debug=True)
    app.run(debug=True, host="0.0.0.0", port=int(os.environ.get("PORT", 8080)))


