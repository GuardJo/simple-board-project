import { redirect } from "next/navigation"

export default () => {
  redirect("/articles?page=1");
}
