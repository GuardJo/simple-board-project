"use client"

export default function ErrorPage() {
    return (
        <main className="grid min-h-full place-items-center bg-white px-6 py-24 sm:py-32 lg:px-8">
            <div className="text-center">
                <p className="text-base font-semibold text-red-600">500</p>
                <h1 className="mt-4 text-3xl font-bold tracking-tight text-gray-900 sm:text-5xl">Error 😥</h1>
                <p className="mt-6 text-base leading-7 text-gray-600">문제가 발생하여, 작업을 처리하지 못했습니다.</p>
                <div className="mt-10 flex items-center justify-center gap-x-6">
                    <button
                        className="rounded-md bg-red-600 px-3.5 py-2.5 text-sm font-semibold text-white shadow-sm hover:bg-red-500 focus-visible:outline focus-visible:outline-2 focus-visible:outline-offset-2 focus-visible:outline-indigo-600"
                        onClick={() => history.back()}
                    >
                        Go Back
                    </button>
                </div>
            </div>
        </main>
    );
}