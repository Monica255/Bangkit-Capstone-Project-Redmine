# MACHINE-LEARNING

Machine Learning part in this project is to create an OCR for KTP(Id card)'s verification, automation email to user and create REST-API for our model. We put our model in google drive: [ML-model](https://drive.google.com/drive/folders/1rmI9K7_hZGSo4O98WZJ9EQ1qqJOCq-BM?usp=sharing) because most of all models have a large size.

## OCR KTP(Id Card)

OCR stands for **Optical Character Recognition** or **Optical Character Reader**, this OCR used for read character from object that has a text. Our app requires user's KTP (Id Card) to verified their account, therefore we use this OCR to read name and gender from user's KTP for additional feature that we give to verified user's account we have 2 scenarios to do this OCR:
1. We create a CNN model to classify every character from 0-9 and A-Z, the dataset that we use is from kaggle: [Standard OCR Dataset](https://www.kaggle.com/datasets/preatcher/standard-ocr-dataset). after that we use RoI (Region of Interest) using library openCV to make a bounding box. After we create a bounding box we crop and save the images from the bounding box, and then  we predict each characters from each images CNN model that we already trained.
2. We create a segmentation model using Unet architecture, we create our own dataset and mask our own dataset. after that we make an RoI from that segmentation result to find the contours, then we crop and save the bounding box and use library called pytesseract to extarct text from each bounding box.

## Automation Email

We create an automation email for users that just create their account, we will send an OTP code automatically to their email to informed their OTP code, therefore users abble to create their account successfully

## Build REST-API for OCR Model
We decided to used second scenario which make a segmentation model using Unet architecture to build our bounding box and then extract text from the bounding box using pytesseract. We using flask to build a REST-API for our OCR model. Because our model is too big, therefore we use **install_gdown.py** to download segmentation model that we have create from google drive: [bounding_box_segmentation_5.h5](https://drive.google.com/file/d/1wLaE2mVfVsoM5ym9iRCU4VAfuYrKVBlq/view?usp=sharing). There are also libraries that needed to run the API:

* flask
* pytesseract
* tensorflow
* numpy
* opencv-python
* matplotlib
* firebase-admin

We also have a dotenv (.env) file to run flask with our specific environment:

>>>FLASK_APP=main.py

>>>FLASK_ENV=development

>>>flask run --host=0.0.0.0

and there is run_flask.sh that will run flask model:

>>>export FLASK_APP=${HOME}/ocr/main.py 
  
>>>export FLASK_ENV=development
  
>>>flask run


We need **redmineKey.json** to configure our API with firebase

## Dataset

there are dataset that we used to build our model:

* [Standard OCR Dataset](https://www.kaggle.com/datasets/preatcher/standard-ocr-dataset).
* [Handwritten math symbols dataset](https://www.kaggle.com/datasets/xainano/handwrittenmathsymbols?resource=download)

and for KTP dataset and masking KTP dataset we create it by our own using figma. this dataset used to make a segmentation using Unet architecture

* [ktp dataset and masking](https://www.figma.com/file/JtDQm0pyFqNAvEQ471QRsE/OCR-KTP?node-id=51%3A572)

## References 

These are links that we used as our reference to build OCR model:

* [Optical Character Recognition Using TensorFlow](https://medium.com/analytics-vidhya/optical-character-recognition-using-tensorflow-533061285dd3)
* [OCR Algorithm: Improve and Automate Business Processes](https://indatalabs.com/blog/ocr-automate-business-processes)
* [How did I write an own OCR program using Keras and TensorFlow in Python](https://towardsdatascience.com/how-did-i-train-an-ocr-model-using-keras-and-tensorflow-7e10b241c22b)
* [OCR: Part 1 — Dataset Generation](https://medium.com/@vijendra1125/ocr-part-1-generate-dataset-69509fbce9c1)
* [OCR: Part 2 — OCR using CNN](https://medium.com/@vijendra1125/ocr-part-2-ocr-using-cnn-f43f0cee8016)
* [4 Simple steps in building OCR](https://medium.datadriveninvestor.com/4-simple-steps-in-building-ocr-1f41c66099c1)
* [Building a Complete OCR Engine From Scratch In Python](https://medium.com/geekculture/building-a-complete-ocr-engine-from-scratch-in-python-be1fd184753b)

## Unet Architecture

This is architecture that we used to make a segmentation to build a bounding box:

![u-net-architecture](https://user-images.githubusercontent.com/91602612/171320831-54ae7a9f-6e49-4073-916b-93157cf893d9.png)
