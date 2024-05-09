import Header from "@/components/Header";
import "./globals.css";

import type { Metadata } from "next";

export const metadata: Metadata = {
  title: {
    template: "%s | Simple Board",
    default: "Simple Board",
  },
  description: "Simple Board Project",
};

export default function RootLayout({ children }: Readonly<{ children: React.ReactNode; }>) {
  return (
    <html lang="ko" className="h-full">
      <body className="h-full">
        <Header />
        {children}
      </body>
    </html>
  );
}
