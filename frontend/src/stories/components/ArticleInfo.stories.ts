import ArticleInfo from "@/components/ArticleInfo";
import { Meta, StoryObj } from "@storybook/react";

const meta = {
    title: "components/ArticleInfo",
    component: ArticleInfo,
} satisfies Meta<typeof ArticleInfo>

export default meta;

type Story = StoryObj<typeof ArticleInfo>

export const NoHashtag: Story = {
    args: {
        author: "Tester1",
        createdTime: "2024-07-30 00:13",
    },
};

export const HasHashtag: Story = {
    args: {
        author: "Tester2",
        createdTime: "2024-07-30 00:14",
        hashtagNames: ["test"],
    },
};

export const HasHashtags: Story = {
    args: {
        author: "Tester2",
        createdTime: "2024-07-30 00:14",
        hashtagNames: ["test", "Test2"],
    },
}