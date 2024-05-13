import ArticleTable from "@/components/ArticleTable";
import SearchBar from "@/components/SearchBar";
import { Metadata } from "next";
import { Suspense } from "react";
import loading from "../loading";
import { getArticlePage } from "@/service/ArticleService";

export const metadata: Metadata = {
  title: "게시판",
};

export default async function HomePage() {
  const articles = await getArticlePage();
  return (
    <div className="flex flex-auto flex-col justify-center items-center gap-5">
      <SearchBar />
      <Suspense fallback={loading()}>
        <ArticleTable data={articles}></ArticleTable>
      </Suspense>
    </div>
  );
}
