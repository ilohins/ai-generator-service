# ai-generator-service
This project is designed to utilize AI to generate a meaningful test data for any of your applications.

# Frameworks
- Spring Boot WEB
- Spring AI

# Prerequesties
OPENAI option: You need to set up Open AI API account. Unfortunately, it's not free. 
The application set up to run on gpt-4.0-mini model (cheapest available at the time)

### OR

OLLAMA option: you need to install Ollama app (all OS supported), configure it as a local server and download models.
Despite this option does not require a credit card or an account, it might require a powerful PC for better performance

# How to install OPEN AI
1) Set up you API key as environmental variable OPENAI_API_KEY
   NOTE: you could use any other variables if you update application.properties or pass the variable through IDE
2) Build and run the project as a Java application
3) Start up the application with -Dspring.profiles.active=openai property
   Note: This also could be set up through application.properties file
4) Application will test API key on startup by printing "Application startup joke"

# How to install OPEN AI
1) Set a new environmental variable OLLAMA_LOCAL_HOST with the http://host:port value of your locally deployed ollama server
e.g.: http://192.168.86.29:11434
   NOTE: you could use any other variables if you update application.properties or pass the variable through IDE
2) Build and run the project as a Java application
3) Start up the application with -Dspring.profiles.active=ollama property
    Note: This also could be set up through application.properties file 
4) Application will test API key on startup by printing "Application startup joke"


# CURL command example
curl -X POST localhost:15501/vg/generate/10
