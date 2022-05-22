# MACHINE-LEARNING

Machine Learning part in this project is to create an OCR for KTP(Id card)'s verification and automation email to user

## OCR KTP(Id Card)

OCR stands for **Optical Character Recognition** or **Optical Character Reader**, this OCR used for read character from object that has a text. Our app requires user's KTP (Id Card) to verified their account, therefore we use this OCR to read name and gender from user's KTP for additional feature that we give to verified user's account we have 2 scenarios to do this OCR:
1. We create a CNN model to classify every character from 0-9 and A-Z, the dataset that we use is from kaggle: [Standard OCR Dataset](https://www.kaggle.com/datasets/preatcher/standard-ocr-dataset). after that we use RoI (Region of Interest) using library openCV to make a bounding box. After we create a bounding box we crop and save the images from the bounding box, and then  we predict each characters from each images CNN model that we already trained.
2. We create a segmentation model using Unet architecture, we create our own dataset and mask our own dataset. after that we make an RoI from that segmentation result to find the contours, then we crop and save the bounding box and use library called pytesseract to extarct text from each bounding box.

## Automation Email

We create an automation email for users that just create their account, we will send an OTP code automatically to their email to informed their OTP code, therefore users abble to create their account successfully

