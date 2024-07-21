import { HashtagInfo } from "@/interface";
import { BASE_URL } from "./context";

const baseUrl = BASE_URL

export function getHashTagList() : HashtagInfo[]{
    // TODO API 연동하기
    return [
        {
            id: 1,
            name: "tag1",
        },
        {
            id: 2,
            name: "tag2",
        },
        {
            id: 3,
            name: "tag3",
        },
        {
            id: 4,
            name: "tag4",
        },
        {
            id: 5,
            name: "tag5",
        },
    ];
}