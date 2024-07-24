import { Meta, StoryObj } from "@storybook/react";
import HashtagList from './../components/HashtagList';

const meta = {
    title: "Components/HashtagList",
    component: HashtagList,
} satisfies Meta<typeof HashtagList>

const mockHashtagList = [
    {
        id: 1,
        hashtagName: "tag1",
    },
    {
        id: 2,
        hashtagName: "tag2",
    },
    {
        id: 3,
        hashtagName: "tag3",
    },
    {
        id: 4,
        hashtagName: "tag4",
    },
    {
        id: 5,
        hashtagName: "tag5",
    },
    {
        id: 6,
        hashtagName: "tag6",
    },
    {
        id: 7,
        hashtagName: "tag7",
    },
    {
        id: 8,
        hashtagName: "tag8",
    },
    {
        id: 9,
        hashtagName: "tag9",
    },
];

export default meta;
type Story = StoryObj<typeof HashtagList>;

export const NoData: Story = {
    args: {},
};

export const HasData: Story = {
    args: {
        data: mockHashtagList,
    },
};