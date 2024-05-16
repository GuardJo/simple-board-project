import PaginationBar from "../components/PaginationBar";
import { Meta, StoryObj } from "@storybook/react";

const meta = {
    title: "components/PaginationBar",
    component: PaginationBar,
} satisfies Meta<typeof PaginationBar>

export default meta;
type Story = StoryObj<typeof meta>;

export const Default: Story = {
    args: {
        number: 1,
        totalPage: 10,
    },
};