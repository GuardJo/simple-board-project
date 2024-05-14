import { ArticleList } from "@/interface";
import ArticleTable from "../components/ArticleTable";
import { Meta, StoryObj } from "@storybook/react";

const meta = {
    title: "components/ArticleTable",
    component: ArticleTable,
} satisfies Meta<typeof ArticleTable>

const articles = {
    number: 1,
    totalPage: 1,
    articles: [
        {
            id: 1,
            title: "Test1",
            content: "Test Content",
            creator: "Tester1",
            hashtags: [],
            createTime: "2024-05-15T13:33:88"
        },
    ],
} satisfies ArticleList

export default meta;
type Story = StoryObj<typeof ArticleTable>

export const NoData: Story = {
    args: {},
};

export const Default: Story = {
    args: {
        data: articles,
    }
}