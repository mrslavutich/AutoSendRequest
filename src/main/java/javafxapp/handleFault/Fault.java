package javafxapp.handleFault;

public enum Fault {
    SEND_MESSAGE("Ошибка при отправке",
            "В данный момент Ваш запрос не может быть перенаправлен через СМЭВ в ФОИВ. Необходимо обратиться в техническую поддержку для проверки корректности зарегистированного сервиса в РСМЭВ.",
            Category.SEND_ERROR),
    SERVICE_NOT_AVALIBALE("Cannot perform client request",
            "В данный момент Ваш запрос не может быть перенаправлен через СМЭВ в ФОИВ. Необходимо обратиться в техническую поддержку для проверки корректности зарегистированного сервиса в РСМЭВ.",
            Category.SEND_ERROR),
    INVALID("INVALID", "В запросе указаны некорректные данные. Просьба повторно направить запрос.В случае воспроизведения ошибки необходимо обратиться в техническую поддержку.", Category.FLK),
    SMEV_100005("SMEV-100005: При обработке запроса произошла ошибка: Сертификат просрочен", "Срок действия сертификата электронной подписи истек. Необходимо обратиться в УЦ для перевыпуска сертификата.", Category.SIGNATURE),
    SERVICE_NOT_FOUND("No policies found for service \"serviceId\". Make sure the service is registered correctly and the gateway policies are up to date.", "В данный момент Ваш запрос не может быть перенаправлен через СМЭВ в ФОИВ. Необходимо обратиться в техническую поддержку для проверки корректности зарегистированного сервиса в РСМЭВ.",
            Category.SEND_ERROR),
    PASSWORD_NOT_VALID("Password is not valid.", "Неверный пароль", Category.SIGN),
    CERTIFICATE_NOT_FOUND("certificate not found", "Не найден сертификат", Category.SIGN),
    HDIMEGESTORE_NOT_FOUND("HDImageStore not found", "Не найдено хранилище сертификатов", Category.SIGN),
    KEY_NOT_FOUND("key not found", "Не найден ключ", Category.SIGN),
    ACCESS_SERVICE("Connection timed out: connect", "Адрес сервиса недоступен", Category.SIGN);

    public enum Category {
        SEND_ERROR("Ошибка при отправке"),
        FLK("Ошибка форматно-логического контроля"),
        SIGNATURE("Проверка подписи"),
        SIGN("Ошибка при подписании");
        private String value;
        private Category(String value){
            this.value = value;
        }

        public String getValue() {
            return value;
        }
    }

    public String message;
    public String modifiedMessage;
    public Category category;

    Fault(String message, String modifiedMessage, Category category) {
        this.message = message;
        this.modifiedMessage = modifiedMessage;
        this.category = category;
    }

    public static Fault forValue(String message) {
        for (Fault fault : Fault.values()) {
            if (fault.message.equals(message)) return fault;
        }
        return null;

    }


}
