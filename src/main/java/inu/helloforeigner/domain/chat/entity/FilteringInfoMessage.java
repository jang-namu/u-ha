package inu.helloforeigner.domain.chat.entity;

public enum FilteringInfoMessage {
    // East Asian
    KO("필터링 된 메시지입니다."),
    JA("フィルタリングされたメッセージです."),
    ZH("这是经过筛选的消息。"),

    // English speaking
    EN("This message has been filtered."),
    EN_AU("This message has been filtered."), // Australia
    EN_CA("This message has been filtered."), // Canada
    EN_IE("This message has been filtered."), // Ireland
    EN_NZ("This message has been filtered."), // New Zealand
    EN_GB("This message has been filtered."), // United Kingdom

    // Romance languages
    FR("Ce message a été filtré."),          // France
    FR_BE("Ce message a été filtré."),       // Belgium (French)
    FR_CA("Ce message a été filtré."),       // Canada (French)
    FR_CH("Ce message a été filtré."),       // Switzerland (French)
    ES("Este mensaje ha sido filtrado."),    // Spain
    ES_CL("Este mensaje ha sido filtrado."), // Chile
    ES_CO("Este mensaje ha sido filtrado."), // Colombia
    ES_CR("Este mensaje ha sido filtrado."), // Costa Rica
    ES_MX("Este mensaje ha sido filtrado."), // Mexico
    IT("Questo messaggio è stato filtrato."),
    PT("Esta mensagem foi filtrada."),

    // Germanic languages
    DE("Diese Nachricht wurde gefiltert."),  // Germany
    DE_AT("Diese Nachricht wurde gefiltert."), // Austria
    DE_CH("Diese Nachricht wurde gefiltert."), // Switzerland (German)
    NL("Dit bericht is gefilterd."),         // Netherlands
    NL_BE("Dit bericht is gefilterd."),      // Belgium (Dutch)

    // Nordic languages
    DA("Denne besked er blevet filtreret."), // Denmark
    IS("Þessi skilaboð hafa verið síuð."),   // Iceland
    NO("Denne meldingen har blitt filtrert."),// Norway
    SV("Detta meddelande har filtrerats."),   // Sweden
    FI("Tämä viesti on suodatettu."),        // Finland

    // Slavic languages
    CS("Tato zpráva byla filtrována."),      // Czech Republic
    PL("Ta wiadomość została przefiltrowana."), // Poland
    SK("Táto správa bola filtrovaná."),      // Slovakia
    SL("To sporočilo je bilo filtrirano."),  // Slovenia

    // Other European languages
    EL("Αυτό το μήνυμα έχει φιλτραριστεί."), // Greece
    ET("See sõnum on filtreeritud."),        // Estonia
    HU("Ezt az üzenetet megszűrtük."),       // Hungary
    LV("Šī ziņa ir filtrēta."),              // Latvia
    LT("Ši žinutė buvo filtruota."),         // Lithuania

    // Other languages
    HE("הודעה זו סוננה."),                    // Israel
    TR("Bu mesaj filtrelendi."),             // Turkey

    // Default fallback
    UNDEFINED("This message has been filtered.");

    private final String message;

    FilteringInfoMessage(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    /**
     * Get filtering message by language code.
     * Returns UNDEFINED if language code is not found.
     */
    public static String getByLanguageCode(String languageCode) {
        try {
            return valueOf(languageCode.toUpperCase()).message;
        } catch (IllegalArgumentException e) {
            return UNDEFINED.getMessage();
        }
    }
}