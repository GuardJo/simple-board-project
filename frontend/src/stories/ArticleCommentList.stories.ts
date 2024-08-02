import { Meta, StoryObj } from "@storybook/react";
import ArticleCommentList from './../components/ArticleCommentList';

const meta = {
    title: "components/ArticleCommentList",
    component: ArticleCommentList,
} satisfies Meta<typeof ArticleCommentList>

export default meta;
type Story = StoryObj<typeof ArticleCommentList>

export const NoComments:Story = {
    args: {},
};

export const HasComments: Story = {
    args: {
        data: [
            {
                id: 1,
                creator: "Tester1",
                content: "First Comment",
                createTime: "2024-07-29 23:56",
            },
            {
                id: 2,
                creator: "Tester2",
                content: "Second Comment",
                createTime: "2024-07-29 23:59",
            },
        ],
    },
};