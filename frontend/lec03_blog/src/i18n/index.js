import i18next from "i18next";
import languageDetector from "i18next-browser-languagedetector";
import { initReactI18next } from "react-i18next";
import en from "./translations/en.json";
import ko from "./translations/ko.json";

i18next
  .use(languageDetector)
  .use(initReactI18next)
  .init({
    resources: {
      en,
      ko,
    },
    load: "languageOnly",
    ns: ["common"],
    defaultNS: "common",
    fallbackLng: "en",
    whitelist: ["ko", "en"],
    debug: false,
    interpolation: {
      escapeValue: false,
    },
  });
