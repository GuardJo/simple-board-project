import HashtagBadge from "../components/HashtagBadge";
import { Meta, StoryObj } from "@storybook/react";

const meta = {
    title: "Components/HashtagBadge",
    component: HashtagBadge,
} satisfies Meta<typeof HashtagBadge>

export default meta;
type Story = StoryObj<typeof HashtagBadge>;

export const Default: Story = {
    args: {
        hashtagName: "Test Tag",
    },
};