import { redirect } from "next/navigation"

export default function RootPage() {
  redirect("/articles?page=1");
}
