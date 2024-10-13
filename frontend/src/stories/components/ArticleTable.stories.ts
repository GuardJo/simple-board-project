import ArticleTable from "@/components/ArticleTable";
import { ArticleList } from "@/interface";
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
        {
            id: 2,
            title: "Test2",
            content: "Test Content2",
            creator: "Tester2",
            hashtags: [
                {
                    id: 1,
                    hashtagName: "TestTag1",
                },
                {
                    id: 2,
                    hashtagName: "TestTag2",
                },
            ],
            createTime: "2024-05-15T13:40:33"
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