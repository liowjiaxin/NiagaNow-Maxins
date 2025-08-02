from fastapi import FastAPI, Request
from fastapi.responses import JSONResponse
from fastapi.middleware.cors import CORSMiddleware
from pydantic import BaseModel
from loan_provider_model import load_model
from chatbot_logic import get_bot_reply, detect_language
import numpy as np

app = FastAPI()

# Enable CORS for all origins (you can restrict this later)
app.add_middleware(
    CORSMiddleware,
    allow_origins=["*"],
    allow_methods=["*"],
    allow_headers=["*"],
)

# --- Loan Provider Model Setup ---
model, label_encoder = load_model()

class ProviderRequest(BaseModel):
    loan_amount: float
    tenure_years: float

@app.post("/predict_provider")
async def predict_provider(data: ProviderRequest):
    try:
        input_data = np.array([[data.loan_amount, data.tenure_years]])
        prediction_encoded = model.predict(input_data)[0]
        predicted_provider = label_encoder.inverse_transform([prediction_encoded])[0]
        return {"provider": predicted_provider}
    except Exception as e:
        return JSONResponse(status_code=500, content={"error": str(e)})


# --- Chatbot Logic Endpoint ---
@app.post("/chat")
async def chat(request: Request):
    data = await request.json()
    user_input = data.get("message", "")
    lang = detect_language(user_input)
    reply = get_bot_reply(user_input)
    return {
        "response": reply,
        "language": lang
    }
