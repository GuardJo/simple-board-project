import Header from "@/components/Header";
import "./globals.css";

import type { Metadata } from "next";
import Footer from "@/components/Footer";
import { me } from "@/service/LoginService";

export const metadata: Metadata = {
  title: {
    template: "%s | Simple Board",
    default: "Simple Board",
  },
  description: "Simple Board Project",
};

export default async function RootLayout({ children }: Readonly<{ children: React.ReactNode; }>) {
  const isLogin: Boolean = await me()
    .then(() => true)
    .catch(() => false);

  return (
    <html lang="ko" className="h-full">
      <body className="h-full pb-20">
        <Header isLogin={isLogin} />
        {children}
        <Footer />
      </body>
    </html>
  );
}
