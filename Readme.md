# CricIndiaWinPredictor

This project is created as part of my Techgig Webinar on Predictive Analytics with R and Java.
 
 http://www.techgig.com/webinar/Predictive-Analytics-using-R-and-Java-964
 
 Predicts whether India will win a Cricket Match depending on scores stats of Virat Kohli. Uses Decision Tree, Random Forest and Neural Net based prediction models.
 
 This project contains examples of how to use Decision Tree, Random Forest and Neural Net models exported in PMML format in a Java Program.
 
 There are three main Java classes DTreePredictor, NeuralNetPredictor and RandomForestPredictor. Each can be executed individually. Aforesaid classes 
 make use of JPMML Evaluator library http://github.com/jpmml/jpmml-evaluator to read PMML models stored in <project__root>/model directory. These PMML
 Models were exported from R. 
 
 Cricket stat data was obtained in YAML format from http://cricsheet.org
 