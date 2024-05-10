import SearchBar from "@/components/SearchBar";
import { Metadata } from "next";

export const metadata: Metadata = {
  title: "게시판",
};

export default function HomePage() {
  return (
    <div className="flex flex-auto justify-center">
      <SearchBar />
    </div>
  );
}
