from fastapi import FastAPI, Request
from fastapi.responses import JSONResponse
from fastapi.middleware.cors import CORSMiddleware
from pydantic import BaseModel
from loan_provider_model import load_model
import numpy as np

app = FastAPI()

# Enable CORS (if calling from Android app or web frontend)
app.add_middleware(
    CORSMiddleware,
    allow_origins=["*"],  # Or specify your frontend origin
    allow_methods=["*"],
    allow_headers=["*"],
)

# Load your model and encoder once
model, label_encoder = load_model()

# Define request schema
class ProviderRequest(BaseModel):
    loan_amount: float
    tenure_years: float

# Define prediction endpoint
@app.post("/predict_provider")
async def predict_provider(data: ProviderRequest):
    try:
        input_data = np.array([[data.loan_amount, data.tenure_years]])
        prediction_encoded = model.predict(input_data)[0]
        predicted_provider = label_encoder.inverse_transform([prediction_encoded])[0]
        return {"provider": predicted_provider}
    except Exception as e:
        return JSONResponse(status_code=500, content={"error": str(e)})
