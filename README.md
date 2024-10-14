# ai-magnet-generator-service
This project is designed to utilize AI to generate a meaningful test data for any of your applications.

# Frameworks
- Spring Boot WEB
- Spring AI

# Prerequesties
You need to set up Open AI API account. Unfortunately, it's not free. 
The application set up to run on gpt-4.0-mini model (cheapest available at the time)

# How to install
1) Set up you API key as environmental variable OPENAI_API_KEY
NOTE: you could use any other variables if you update application.properties
2) Build and run the project as a Java application
3) Application will test API key on startup by printing "Application startup joke"

# CURL command example
curl -X POST localhost:15501/vg/generate/10
