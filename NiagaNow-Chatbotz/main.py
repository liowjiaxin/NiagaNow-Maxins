from fastapi import FastAPI, Request
from fastapi.middleware.cors import CORSMiddleware
from chatbot_logic import get_bot_reply, detect_language

app = FastAPI()

app.add_middleware(
    CORSMiddleware,
    allow_origins=["*"],
    allow_methods=["*"],
    allow_headers=["*"],
)

@app.post("/chat")
async def chat(request: Request):
    data = await request.json()
    user_input = data.get("message", "")
    lang = detect_language(user_input)
    reply = get_bot_reply(user_input)
    return {
        "response": reply,
        "language": lang   # ✅ Add this line
    }
