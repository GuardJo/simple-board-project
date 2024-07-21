import { Meta, StoryObj } from "@storybook/react";
import HashtagList from './../components/HashtagList';

const meta = {
    title: "Components/HashtagList",
    component: HashtagList,
} satisfies Meta<typeof HashtagList>

const mockHashtagList = [
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
    {
        id: 6,
        name: "tag6",
    },
    {
        id: 7,
        name: "tag7",
    },
    {
        id: 8,
        name: "tag8",
    },
    {
        id: 9,
        name: "tag9",
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