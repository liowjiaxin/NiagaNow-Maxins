from rapidfuzz import fuzz

# Conversation context to track last topic and history
conversation_context = {
    "last_topic": None,
    "last_language": None,
    "history": []
}

def fuzzy_keyword_match(text, keywords, threshold=80):
    tokens = text.lower().split()
    for token in tokens:
        for kw in keywords:
            if fuzz.partial_ratio(token, kw.lower()) >= threshold:
                return True
    return False

def detect_language(text):
    bm_phrases = ["macam mana", "mcm mana", "saya perlu", "boleh tak", "ada kah", "bagaimana", "cara nak", "nak apply", "nak pinjam"]
    zh_phrases = ["怎么办", "需要什么", "可以吗", "有没有", "如何"]
    text = text.lower()

    if any(phrase in text for phrase in bm_phrases):
        return "bm"
    if any(phrase in text for phrase in zh_phrases):
        return "zh"

    bm_keywords = ["pinjaman", "duitnow", "geran", "saya", "bantuan", "dokumen", "akaun", "pengenalan","pinjam"]
    zh_keywords = ["贷款", "扫码", "资助", "我", "帮助", "文件", "账户", "身份证"]

    bm_count = sum(word in text for word in bm_keywords)
    zh_count = sum(word in text for word in zh_keywords)

    if bm_count > zh_count:
        return "bm"
    elif zh_count > bm_count:
        return "zh"
    return "en"

def respond_in_bm(msg):
    global conversation_context
    if fuzzy_keyword_match(msg, ["dokumen", "perlu", "require", "need"]):
        if "pinjaman" in conversation_context["history"] or "loan" in conversation_context["history"]:
            return ("Untuk pinjaman, anda perlu:\n1. Kad Pengenalan\n2. Penyata bank 3 bulan terkini\n3. Dokumen perniagaan (jika berkenaan)\n\nKami boleh jana senarai semak khusus untuk anda!")
        elif "geran" in conversation_context["history"] or "grant" in conversation_context["history"]:
            return ("Untuk geran SME:\n1. SSM terkini\n2. Laporan kewangan 6 bulan\n3. Pengenalan pemilik perniagaan")
        else:
            return "Sila nyatakan sama ada untuk pinjaman atau geran SME."
    if fuzzy_keyword_match(msg, ["proses", "cara", "steps", "how to"]):
        if "pinjaman" in conversation_context["history"]:
            return ("Proses pinjaman:\n1. Terima bayaran konsisten 2 minggu\n2. Lengkapkan profil anda\n3. Semak kelayakan automatik\n4. Hantar dokumen\n\nKami pandu anda langkah demi langkah!")
    if fuzzy_keyword_match(msg, ["pinjaman", "pinjam", "duit"]):
        conversation_context["last_topic"] = "pinjaman"
        conversation_context["history"].append("pinjaman")
        return ("Untuk memohon pinjaman:\n1. Pastikan penerimaan bayaran konsisten 2 minggu\n2. Analisis transaksi kami akan tentukan kelayakan anda\n3. Kami beri senarai dokumen khusus")
    if fuzzy_keyword_match(msg, ["geran"]):
        conversation_context["last_topic"] = "geran"
        conversation_context["history"].append("geran")
        return ("Untuk geran SME:\n• Layak jika ada transaksi konsisten\n• Kami bantu dengan proses permohonan\n• Dapatkan panduan dokumen khusus")
    if fuzzy_keyword_match(msg, ["qr", "duitnow"]):
        conversation_context["last_topic"] = "qr"
        conversation_context["history"].append("qr")
        return ("Kod DuitNow QR:\n• Dapatkan dalam tab Papan Pemuka\n• Buat promosi dengan QR flyer WhatsApp\n• Auto-jana 'Scan to Pay' visual")
    if fuzzy_keyword_match(msg, ["daftar", "akaun"]):
        conversation_context["last_topic"] = "onboarding"
        conversation_context["history"].append("onboarding")
        return ("Pendaftaran mudah:\n1. Download aplikasi NiagaNow\n2. Daftar dengan telefon & IC sahaja\n3. Dapatkan QR serta-merta\n\nTiada dokumen perniagaan diperlukan!")
    if fuzzy_keyword_match(msg, ["pemasaran", "promosi"]):
        conversation_context["last_topic"] = "marketing"
        conversation_context["history"].append("marketing")
        return ("Alat pemasaran:\n• Auto-jana QR flyer untuk WhatsApp\n• Buat pos media sosial automatik\n• Visual 'Scan to Pay Me' profesional")
    if fuzzy_keyword_match(msg, ["keselamatan", "login"]):
        conversation_context["last_topic"] = "security"
        conversation_context["history"].append("security")
        return ("Keselamatan:\n• Gunakan cap jari atau Face ID\n• Autentikasi 2-faktor\n• Data anda dilindungi")
    if fuzzy_keyword_match(msg, ["bantuan", "tolong"]):
        return ("Saya boleh bantu dengan:\n• Pinjaman & geran\n• Kod QR & DuitNow\n• Pendaftaran & onboarding\n• Pemasaran & promosi\n• Keselamatan akaun")
    else:
        if conversation_context["last_topic"] == "pinjaman":
            return "Adakah anda ingin tahu tentang kelayakan atau dokumen untuk pinjaman?"
        elif conversation_context["last_topic"] == "geran":
            return "Adakah anda ingin tahu proses atau dokumen untuk geran SME?"
        return "Maaf, boleh anda jelaskan lagi? Saya boleh bantu dengan pinjaman, geran, QR, atau pendaftaran."

def respond_in_zh(msg):
    global conversation_context
    if fuzzy_keyword_match(msg, ["文件", "需要", "资料"]):
        if "贷款" in conversation_context["history"]:
            return ("申请贷款所需文件：\n1. 身份证\n2. 最近3个月银行账单\n3. 商业注册文件（如适用）\n\n我们可以为您生成个性化清单！")
        elif "资助" in conversation_context["history"]:
            return ("申请中小企业资助所需文件：\n1. 最新的SSM商业注册文件\n2. 最近6个月财务报表\n3. 业务拥有者的身份证")
        else:
            return "请说明您需要贷款还是资助的资料？"
    if fuzzy_keyword_match(msg, ["流程", "怎么", "步骤"]):
        if "贷款" in conversation_context["history"]:
            return ("贷款流程：\n1. 连续两周有稳定收入记录\n2. 完善您的个人资料\n3. 系统自动评估资格\n4. 上传所需文件\n\n我们会逐步引导您完成！")
        elif "注册" in conversation_context["history"]:
            return ("注册流程：\n1. 下载NiagaNow应用\n2. 仅需手机号码和身份证即可注册\n3. 马上获得DuitNow二维码\n\n无需提交商业文件！")
    if fuzzy_keyword_match(msg, ["贷款", "借款"]):
        conversation_context["last_topic"] = "贷款"
        conversation_context["history"].append("贷款")
        return ("申请贷款条件：\n1. 连续两周有稳定的付款记录\n2. 我们会分析交易来判断资格\n3. 我们会提供所需文件清单")
    if fuzzy_keyword_match(msg, ["资助", "grant"]):
        conversation_context["last_topic"] = "资助"
        conversation_context["history"].append("资助")
        return ("中小企业资助：\n• 有稳定的交易记录即可符合资格\n• 我们协助您完成申请流程\n• 获取所需文件的详细指导")
    if fuzzy_keyword_match(msg, ["扫码", "qr", "二维码", "duitnow"]):
        conversation_context["last_topic"] = "qr"
        conversation_context["history"].append("qr")
        return ("DuitNow二维码：\n• 可在仪表板中查看\n• 自动生成WhatsApp宣传图\n• 制作“扫码付款”视觉海报")
    if fuzzy_keyword_match(msg, ["注册", "账户"]):
        conversation_context["last_topic"] = "注册"
        conversation_context["history"].append("注册")
        return ("注册步骤：\n1. 下载NiagaNow应用\n2. 使用手机号码和身份证注册\n3. 即刻获得二维码\n\n不需要提交商业文件！")
    if fuzzy_keyword_match(msg, ["推广", "营销", "宣传"]):
        conversation_context["last_topic"] = "推广"
        conversation_context["history"].append("推广")
        return ("营销工具：\n• 自动生成WhatsApp宣传图\n• 快速制作社交媒体海报\n• 专业‘扫码付款’视觉模板")
    if fuzzy_keyword_match(msg, ["安全", "登录", "认证"]):
        conversation_context["last_topic"] = "安全"
        conversation_context["history"].append("安全")
        return ("账户安全：\n• 支持指纹或人脸识别登录\n• 启用双重验证（2FA）\n• 数据加密存储，保障商户隐私")
    if fuzzy_keyword_match(msg, ["帮助", "帮我", "客服"]):
        return ("我可以协助您了解：\n• 贷款和中小企业资助\n• DuitNow二维码设置\n• 注册与账户创建\n• 营销推广工具\n• 账户安全设置")
    else:
        if conversation_context["last_topic"] == "贷款":
            return "您想了解贷款的资格条件还是所需文件？"
        elif conversation_context["last_topic"] == "资助":
            return "您是想了解资助流程还是所需文件？"
        return "对不起，我没明白您的意思。可以请您再说明一下吗？我可以协助贷款、资助、二维码或注册相关的问题。"

def respond_in_en(msg):
    global conversation_context
    if fuzzy_keyword_match(msg, ["document", "require", "need"]):
        if "loan" in conversation_context["history"]:
            return ("For loans you need:\n1. Identification document\n2. 3 months bank statements\n3. Business docs (if applicable)\n\nWe can generate a personalized checklist!")
        elif "grant" in conversation_context["history"]:
            return ("For SME grants:\n1. Business registration\n2. 6 months financial statements\n3. Business owner ID")
        else:
            return "Please specify if you're asking about loan or grant documents."
    if fuzzy_keyword_match(msg, ["process", "how to", "steps"]):
        if "loan" in conversation_context["history"]:
            return ("Loan process:\n1. Receive payments consistently for 2 weeks\n2. Complete your profile\n3. Check automatic eligibility\n4. Submit documents\n\nWe'll guide you step-by-step!")
        elif "onboarding" in conversation_context["history"]:
            return ("Onboarding:\n1. Download NiagaNow app\n2. Register with phone & IC only\n3. Get QR immediately\n\nNo business documents needed!")
    if fuzzy_keyword_match(msg, ["loan"]):
        conversation_context["last_topic"] = "loan"
        conversation_context["history"].append("loan")
        return ("To apply for a loan:\n1. Ensure consistent payments for 2 weeks\n2. Our transaction analysis determines eligibility\n3. Get personalized document checklist")
    if fuzzy_keyword_match(msg, ["grant"]):
        conversation_context["last_topic"] = "grant"
        conversation_context["history"].append("grant")
        return ("For SME grants:\n• Eligible with consistent transactions\n• We assist with application process\n• Get custom document guidance")
    if fuzzy_keyword_match(msg, ["qr", "duitnow"]):
        conversation_context["last_topic"] = "qr"
        conversation_context["history"].append("qr")
        return ("Your DuitNow QR:\n• Found in Dashboard tab\n• Create WhatsApp QR flyers\n• Auto-generate 'Scan to Pay' visuals")
    if fuzzy_keyword_match(msg, ["register", "onboarding", "account"]):
        conversation_context["last_topic"] = "onboarding"
        conversation_context["history"].append("onboarding")
        return ("Easy onboarding:\n1. Download NiagaNow app\n2. Register with phone & ID only\n3. Get QR code immediately\n\nNo business docs required upfront!")
    if fuzzy_keyword_match(msg, ["market", "promot"]):
        conversation_context["last_topic"] = "marketing"
        conversation_context["history"].append("marketing")
        return ("Marketing tools:\n• Auto-create WhatsApp QR flyers\n• Generate social media posts\n• Professional 'Scan to Pay Me' visuals")
    if fuzzy_keyword_match(msg, ["security", "login", "authentic"]):
        conversation_context["last_topic"] = "security"
        conversation_context["history"].append("security")
        return ("Security features:\n• Fingerprint or Face ID login\n• Two-factor authentication\n• Protected merchant data")
    if fuzzy_keyword_match(msg, ["help"]):
        return ("I can help with:\n• Loans & grants\n• QR codes & DuitNow\n• Registration & onboarding\n• Marketing tools\n• Account security")
    else:
        if conversation_context["last_topic"] == "loan":
            return "Would you like to know about eligibility or required documents for loans?"
        elif conversation_context["last_topic"] == "grant":
            return "Would you like to know about the application process or documents for grants?"
        return "Could you clarify? I can help with loans, grants, QR codes, or registration."

def get_bot_reply(user_input):
    lang = detect_language(user_input)
    conversation_context["last_language"] = lang
    msg = user_input.lower()

    if lang == "bm":
        return respond_in_bm(msg)
    elif lang == "zh":
        return respond_in_zh(msg)
    else:
        return respond_in_en(msg)
